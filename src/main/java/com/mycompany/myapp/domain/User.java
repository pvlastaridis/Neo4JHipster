package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * A user.
 */

@SuppressWarnings("serial")
@NodeEntity
public class User extends AbstractAuditingEntity implements Serializable{

	@GraphId
	Long id;

	@NotNull
	@Indexed
	public String login;
	
	@JsonIgnore
    @Size(min = 0, max = 100)
	public String password;
	
 	
 	@Size(min = 0, max = 50)
	public String firstName;


	@Size(min = 0, max = 50)
	public String lastName;
	
	@Email
    @Size(min = 0, max = 100)
	public String email;
	
	public boolean activated;
	

	@Size(min = 2, max = 5)
	public String langKey;
	
	@Size(min = 0, max = 20)
	public String activationKey;

    @JsonIgnore
	@RelatedTo(direction = Direction.OUTGOING)
	@Fetch
	Set<Authority> authorities = new HashSet<Authority>();

	@RelatedTo(direction = Direction.INCOMING)
	@Fetch
    private Set<PersistentToken> persistentTokens = new HashSet<>();

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "User{" + "login='" + login + '\'' + ", password='" + password
				+ '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
				+ lastName + '\'' + ", email='" + email + '\''
				+ ", activated='" + activated + '\'' + ", langKey='" + langKey
				+ '\'' + ", activationKey='" + activationKey + '\'' + "}";
	}
}
