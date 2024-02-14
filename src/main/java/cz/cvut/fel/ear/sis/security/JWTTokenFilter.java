package cz.cvut.fel.ear.sis.security;

import cz.cvut.fel.ear.sis.security.model.JWTAuthenticationToken;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import cz.cvut.fel.ear.sis.service.security.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JWTTokenFilter extends OncePerRequestFilter {
    UserDetailService userDetailService;
    @Autowired
    public void setUserDetailService(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            try {
                SISUserDetails userDetails = userDetailService.loadUserByUsername(JWTUtils.extractUsername(token));

                SecurityContextHolder.getContext().setAuthentication(new JWTAuthenticationToken(token, userDetails));
            } catch (ExpiredJwtException e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                return;
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // Extract the token from the Authorization header or another location in the request
        // Be sure to handle different token extraction methods (e.g., from headers, query parameters, cookies)
        // Return null if no token is found
        return request.getHeader("jwt-token");
    }
}
