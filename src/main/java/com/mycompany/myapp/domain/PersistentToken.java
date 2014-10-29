package com.mycompany.myapp.domain;

//import java.text.SimpleDateFormat;
import java.util.Date;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@NodeEntity
public class PersistentToken {

	
	private static final int MAX_USER_AGENT_LEN = 255;

	//SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
	
	@GraphId
	Long id;
	
	@Indexed
	public String series;
	
    private String tokenValue;
    @Indexed
    private Long tokenDate;
    
    private String ipAddress;
    private String userAgent;
    
    @RelatedTo(type = "PERSISTENT_TOKENS", direction = Direction.OUTGOING)
	@Fetch
    private User user;

	
	public PersistentToken(){}
	
	
	 
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
	        Date tokDate = new Date(this.tokenDate);
	        return tokDate.toString();
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

