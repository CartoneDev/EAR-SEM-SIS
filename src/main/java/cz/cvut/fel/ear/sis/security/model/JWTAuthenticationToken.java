package cz.cvut.fel.ear.sis.security.model;

import cz.cvut.fel.ear.sis.security.JWTUtils;
import cz.cvut.fel.ear.sis.service.security.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.util.Collection;

public class JWTAuthenticationToken implements Authentication {
    private final String token;
    private boolean isAuthenticated;
    private SISUserDetails userDetails;
    private Collection<? extends GrantedAuthority> authorities;

    public JWTAuthenticationToken(String token, SISUserDetails userDetails) {
        this.token = token;
        this.authorities = userDetails.getAuthorities();
        isAuthenticated = true;
        if (JWTUtils.isTokenExpired(token)) {
            isAuthenticated = false;
            throw new ExpiredJwtException(null, JWTUtils.extractAllClaims(token), "Token expired");
        }
        this.userDetails = userDetails;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return JWTUtils.extractUsername(token);
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated && !JWTUtils.isTokenExpired(token);
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }
}
