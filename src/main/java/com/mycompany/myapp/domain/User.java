package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@NodeEntity
public class User extends Entity {

    @NotNull
    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
    @Size(min = 1, max = 50)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    @Size(max = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    private String resetKey;

    private Long createdDate = null;

    private Long resetDate = null;

    @JsonIgnore
    private Set<Authority> authorities = new HashSet<>();

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

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Long getResetDate() {
        return resetDate;
    }

    public void setResetDate(Long resetDate) {
        this.resetDate = resetDate;
    }

    @JsonIgnore
    public ZonedDateTime getResetDDate() {
        Instant instant = Instant.ofEpochSecond(resetDate);
        ZonedDateTime ldt = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        return ldt;
    }

    public void setResetDDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate.toEpochSecond();
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long resetDate) {
        this.createdDate = resetDate;
    }

    @JsonIgnore
    public ZonedDateTime getCreatedDDate() {
        Instant instant = Instant.ofEpochMilli(createdDate);
        ZonedDateTime ldt = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        return ldt;
    }

    public void setCreatedDDate(ZonedDateTime resetDate) {
        this.createdDate = resetDate.toEpochSecond();
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
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
