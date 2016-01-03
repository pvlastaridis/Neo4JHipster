package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Persistent tokens are used by Spring Security to automatically log in users.
 *
 * @see com.mycompany.myapp.security.CustomPersistentRememberMeServices
 */
@NodeEntity
public class PersistentToken extends Entity {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy");
    private static final int MAX_USER_AGENT_LEN = 255;

    private String series;

    @JsonIgnore
    @NotNull
    private String tokenValue;

    @JsonIgnore
    private Long tokenDate;

    //an IPV6 address max length is 39 characters
    @Size(min = 0, max = 39)
    private String ipAddress;

    private String userAgent;

    @JsonIgnore
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

    public Long getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(Long tokenDate) {
        this.tokenDate = tokenDate;
    }

    @JsonGetter
    public String getFormattedTokenDate() {
        LocalDate ldt = LocalDate.ofEpochDay(tokenDate);
        return DATE_TIME_FORMATTER.format(ldt);
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
