package cz.cvut.fel.ear.sis.security;

import cz.cvut.fel.ear.sis.security.model.JWTAuthenticationToken;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import cz.cvut.fel.ear.sis.service.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailService userDetailService;

    @Autowired
    public JWTAuthenticationProvider(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        String username = JWTUtils.extractUsername(token);

        SISUserDetails userDetails = userDetailService.loadUserByUsername(username);

        if (JWTUtils.validateToken(token, userDetails)) {
            return new JWTAuthenticationToken(token, userDetails);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
