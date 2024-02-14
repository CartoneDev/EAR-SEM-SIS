package cz.cvut.fel.ear.sis.controller;

import com.hazelcast.org.slf4j.Logger;
import com.hazelcast.org.slf4j.LoggerFactory;
import cz.cvut.fel.ear.sis.security.JWTUtils;
import cz.cvut.fel.ear.sis.security.model.JWTAuthenticationToken;
import cz.cvut.fel.ear.sis.security.model.LoginRequest;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import cz.cvut.fel.ear.sis.service.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserDetailService userDetailService;


    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationController(UserDetailService userDetailService, AuthenticationManager authenticationManager) {
        this.userDetailService = userDetailService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        LOG.info("AAA");
        try {
            SISUserDetails userDetails = userDetailService.loadUserByUsername(loginRequest.getUsername());

            userDetailService.verifyPassword(userDetails, loginRequest.getPassword());
            String token = JWTUtils.generateToken(userDetails);
            JWTAuthenticationToken authenticationToken = new JWTAuthenticationToken(token, userDetails);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);



            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/logout")
    public void logout() {
    }
}
