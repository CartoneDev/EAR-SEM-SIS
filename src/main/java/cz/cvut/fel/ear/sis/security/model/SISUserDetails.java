package cz.cvut.fel.ear.sis.security.model;

import cz.cvut.fel.ear.sis.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SISUserDetails implements UserDetails {
    private final User user;

    public SISUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        if (user.getClass() != User.class) {
            authorities.add(new SimpleGrantedAuthority(user.getClass().getSimpleName().toUpperCase(Locale.ROOT)));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordFingerprint();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
