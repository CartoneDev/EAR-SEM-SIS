package cz.cvut.fel.ear.sis.security;


import cz.cvut.fel.ear.sis.config.JWTConfig;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JWTUtils {
    public static String generateToken(SISUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails.getAuthorities() != null) {
            claims.put("authorities", userDetails.getAuthorities());
        }
        return createToken(claims, userDetails.getUsername());
    }

    public static String createToken(Map<String, Object> claims, String subject) {
        claims.put("username", subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.validityInMs))
                .signWith(JWTConfig.key())
                .compact();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(JWTConfig.key()).parseClaimsJws(token).getBody();
        return (Claims) Jwts.parserBuilder().setSigningKey(JWTConfig.key()).build().parse(token).getBody();
    }
}

