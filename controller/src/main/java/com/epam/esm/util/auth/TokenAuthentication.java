package com.epam.esm.util.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication implements Authentication {

    private String token;
    private Collection<? extends GrantedAuthority> authorities;
    private String principal;
    private boolean isAuthenticated;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public TokenAuthentication() {
    }

    public TokenAuthentication(String token, Collection<? extends GrantedAuthority> authorities,
                               String principal, boolean isAuthenticated) {
        this.token = token;
        this.authorities = authorities;
        this.principal = principal;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal;
    }

    public String getToken() {
        return token;
    }
}
