package com.mycompany.myapp.security;

import org.joda.time.LocalDate;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.PersistentToken;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.PersistentTokenRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.SecureRandom;
import java.util.Arrays;


/**
 * Custom implementation of Spring Security's RememberMeServices.
 * <p/>
 * Persistent tokens are used by Spring Security to automatically log in users.
 * <p/>
 * This is a specific implementation of Spring Security's remember-me
 * authentication, but it is much more powerful than the standard
 * implementations:
 * <ul>
 * <li>It allows a user to see the list of his currently opened sessions, and
 * invalidate them</li>
 * <li>It stores more information, such as the IP address and the user agent,
 * for audit purposes
 * <li>
 * <li>When a user logs out, only his current session is invalidated, and not
 * all of his sessions</li>
 * </ul>
 * <p/>
 * This is inspired by:
 * <ul>
 * <li><a
 * href="http://jaspan.com/improved_persistent_login_cookie_best_practice"
 * >Improved Persistent Login Cookie Best Practice</a></li>
 * <li><a
 * href="https://github.com/blog/1661-modeling-your-app-s-user-session">Github's
 * "Modeling your App's User Session"</a></li></li>
 * </ul>
 * <p/>
 * The main algorithm comes from Spring Security's
 * PersistentTokenBasedRememberMeServices, but this class couldn't be cleanly
 * extended.
 * <p/>
 */
@Service
public class CustomPersistentRememberMeServices extends
		AbstractRememberMeServices {

	private final Logger log = LoggerFactory
			.getLogger(CustomPersistentRememberMeServices.class);

	// Token is valid for one month
	private static final int TOKEN_VALIDITY_DAYS = 31;

	private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;

	private static final int DEFAULT_SERIES_LENGTH = 16;

	private static final int DEFAULT_TOKEN_LENGTH = 16;

	private SecureRandom random;

	@Inject
	private PersistentTokenRepository persistentTokenRepository;

	@Inject
	Neo4jTemplate neotemp;

	@Inject
	public CustomPersistentRememberMeServices(
			Environment env,
			org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {

		super("hello", userDetailsService);
		random = new SecureRandom();
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("Entering processAutoLoginCookie Function");
		PersistentToken token = getPersistentToken(cookieTokens);
		String login = token.getUser().getLogin();

		// Token also matches, so login is valid. Update the token value,
		// keeping the *same* series number.
		log.info("Matched token found for user '{}', series '{}'", login,
				token.getSeries());
		token.setTokenDate(new LocalDate());
		token.setTokenValue(generateTokenData());
		token.setIpAddress(request.getRemoteAddr());
		token.setUserAgent(request.getHeader("User-Agent"));
		try {
			persistentTokenRepository.save(token);
			if (request.getServletContext().getAttribute("rememberME") != null) {
				if (request.getServletContext().getAttribute("rememberME")
						.toString().equalsIgnoreCase("true")) {
					addCookie(token, request, response);
					log.info("Adding Persistent Cookie");
				}
			 else {
				addSessionCookie(token, request, response);
				log.info("Adding Session Cookie");

			}
			} else {
				addSessionCookie(token, request, response);
				log.info("Adding Session Cookie");

			}
		} catch (DataAccessException e) {
			log.error("Failed to update token: ");
			throw new RememberMeAuthenticationException(
					"Autologin failed due to data access problem", e);
		}

		return getUserDetailsService().loadUserByUsername(login);
	}

	@Override
	protected boolean rememberMeRequested(HttpServletRequest request,
			String parameter) {
		/*
		 * if (alwaysRemember) { return true; }
		 */

		String paramValue = request.getParameter(parameter);

		if (paramValue != null) {
			if (paramValue.equalsIgnoreCase("true")
					|| paramValue.equalsIgnoreCase("on")
					|| paramValue.equalsIgnoreCase("yes")
					|| paramValue.equals("1")) {
				String rememberME = "true";
				request.getServletContext().setAttribute("rememberME",
						rememberME);
				return true;
			}
		}
		String rememberME = "false";
		request.getServletContext().setAttribute("rememberME", rememberME);

		if (logger.isDebugEnabled()) {
			logger.debug("Did not send remember-me cookie (principal did not set parameter '"
					+ parameter + "')");
		}

		return true;
	}

	@Override
	protected void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication successfulAuthentication) {

		String login = successfulAuthentication.getName();
		log.info("Login Success for user with login: ", login);
		PersistentToken token = new PersistentToken();

		try (Transaction tx = neotemp.getGraphDatabaseService().beginTx()) {

			User user = neotemp.findByIndexedValue(User.class, "login", login)
					.singleOrNull();
			token.setSeries(generateSeriesData());
			token.setTokenValue(generateTokenData());
			token.setUser(user);
			token.setTokenDate(new LocalDate());
			token.setIpAddress(request.getRemoteAddr());
			token.setUserAgent(request.getHeader("User-Agent"));
			try {
				token = persistentTokenRepository.save(token);
				// user.getPersistentTokens().add(token);
				// usrrepo.save(user);
				log.info("Saved new persistent token for user: " + login);

			} catch (DataAccessException e) {
				log.error("Failed to save new persistent token ", e);
			}

			tx.success();
		}
		log.info("_spring_security_remember_me: "
				+ request.getParameter("_spring_security_remember_me"));
		if (request.getParameter("_spring_security_remember_me").toString()
				.equalsIgnoreCase("true")) {
			addCookie(token, request, response);
			String rememberME = "true";
			request.getServletContext().setAttribute("rememberME", rememberME);

			log.info("Adding persistent Cookie");
		} else {
			addSessionCookie(token, request, response);
			String rememberME = "false";
			request.getServletContext().setAttribute("rememberME", rememberME);

			log.info("Adding Session Cookie");

		}

	}

	/**
	 * When logout occurs, only invalidate the current token, and not all user
	 * sessions.
	 * <p/>
	 * The standard Spring Security implementations are too basic: they
	 * invalidate all tokens for the current user, so when he logs out from one
	 * browser, all his other sessions are destroyed.
	 */
	@Override
	@Transactional
	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		String rememberMeCookie = extractRememberMeCookie(request);
		if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
			try {
				String[] cookieTokens = decodeCookie(rememberMeCookie);
				PersistentToken token = getPersistentToken(cookieTokens);
				persistentTokenRepository.delete(token);
				log.info("Persistent Token deleted");

			} catch (InvalidCookieException ice) {
				log.info("Invalid cookie, no persistent token could be deleted");
			} catch (RememberMeAuthenticationException rmae) {
				log.debug("No persistent token found, so no token could be deleted");
			}
		}
		super.logout(request, response, authentication);
	}

	/**
	 * Validate the token and return it.
	 */
	private PersistentToken getPersistentToken(String[] cookieTokens) {
		if (cookieTokens.length != 2) {
			throw new InvalidCookieException("Cookie token did not contain "
					+ 2 + " tokens, but contained '"
					+ Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];

		PersistentToken token = persistentTokenRepository
				.findBySeries(presentedSeries);

		if (token == null) {
			// No series match, so we can't authenticate using this cookie
			throw new RememberMeAuthenticationException(
					"No persistent token found for series id: "
							+ presentedSeries);
		}

		// We have a match for this user/series combination
		log.info("presentedToken={} / tokenValue={}", presentedToken,
				token.getTokenValue());
		if (!presentedToken.equals(token.getTokenValue())) {
			// Token doesn't match series value. Delete this session and throw
			// an exception.
			persistentTokenRepository.delete(token);
			//throw new CookieTheftException(
			//		"Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
		}

        if (token.getTokenDate().plusDays(TOKEN_VALIDITY_DAYS).isBefore(LocalDate.now())) {
			persistentTokenRepository.delete(token);
			throw new RememberMeAuthenticationException(
					"Remember-me login has expired");
		}
		return token;
	}

	private String generateSeriesData() {
		byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
		random.nextBytes(newSeries);
		return new String(Base64.encode(newSeries));
	}

	private String generateTokenData() {
		byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
		random.nextBytes(newToken);
		return new String(Base64.encode(newToken));
	}

	private void addSessionCookie(PersistentToken token,
			HttpServletRequest request, HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() },
				-1, request, response);
	}

	private void addCookie(PersistentToken token, HttpServletRequest request,
			HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() },
				TOKEN_VALIDITY_SECONDS, request, response);
	}
}
