package com.mycompany.myapp.domain;

//import java.text.SimpleDateFormat;
import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * Persistent tokens are used by Spring Security to automatically log in users.
 *
 * @see com.mycompany.myapp.security.CustomPersistentRememberMeServices
 */


@SuppressWarnings("serial")
@NodeEntity
public class PersistentToken implements Serializable {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("d MMMM yyyy");

	private static final int MAX_USER_AGENT_LEN = 255;

	
	@GraphId
	Long id;
	
	@Indexed
	public String series;
	
	@JsonIgnore
    @NotNull
    private String tokenValue;
   
    @Indexed
    private Long tokenDate;

    //an IPV6 address max length is 39 characters
    @Size(min = 0, max = 39)
    private String ipAddress;
    

    private String userAgent;
    
	@JsonIgnore
    @RelatedTo(type = "PERSISTENT_TOKENS", direction = Direction.OUTGOING)
	@Fetch
    private User user;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public LocalDate getTokenDate() {
        return new LocalDate(tokenDate);
    }

    public void setTokenDate(LocalDate tokenDate) {
        this.tokenDate = tokenDate.toDate().getTime();
    }

    @JsonGetter
    public String getFormattedTokenDate() {
        return DATE_TIME_FORMATTER.print(new LocalDate(this.tokenDate));
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

	    public String getUserAgent() {
	        return userAgent;
	    }

	    public void setUserAgent(String userAgent) {
	        if (userAgent.length() >= MAX_USER_AGENT_LEN) {
	            this.userAgent = userAgent.substring(0, MAX_USER_AGENT_LEN - 1);
	        } else {
	            this.userAgent = userAgent;
	        }
	    }

	    public User getUser() {
	        return user;
	    }

	    public void setUser(User user) {
	        this.user = user;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }

	        PersistentToken that = (PersistentToken) o;

	        if (!series.equals(that.series)) {
	            return false;
	        }

	        return true;
	    }

	    @Override
	    public int hashCode() {
	        return series.hashCode();
	    }

	    @Override
	    public String toString() {
	        return "PersistentToken{" +
	                "series='" + series + '\'' +
	                ", tokenValue='" + tokenValue + '\'' +
	                ", tokenDate=" + tokenDate +
	                ", ipAddress='" + ipAddress + '\'' +
	                ", userAgent='" + userAgent + '\'' +
	                "}";
	    }
	}
