package cz.cvut.fel.ear.sis.config;

import cz.cvut.fel.ear.sis.security.JWTAuthenticationProvider;
import cz.cvut.fel.ear.sis.security.JWTTokenFilter;
import cz.cvut.fel.ear.sis.service.security.UserDetailService;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;

import java.security.Key;

@Getter
@Configuration
public class JWTConfig {
    @Value("${jwt.secret}")
    public static String secret = "|dCreHbmx<PD(VERYSECUREYEAH)S#mN)GS~d_UPjFYfFK";
    public final static long validityInMs = 60 * 60 * 1000; // 1h
    private static UserDetailService userDetailService;

    @Autowired
    public JWTConfig(UserDetailService userDetailService) {
        JWTConfig.userDetailService = userDetailService;
    }

    @Bean
    public static Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    @Bean
    public JWTTokenFilter jwtTokenFilter() {
        return new JWTTokenFilter();
    }

    @Bean
    public FilterRegistrationBean<JWTTokenFilter> jwtFilter() {
        FilterRegistrationBean<JWTTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtTokenFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("exclusions", "/login,/logout,/register,/auth/login");
        return registrationBean;
    }

    @Bean("jwt_provider")
    public JWTAuthenticationProvider JWT() {
        return new JWTAuthenticationProvider(userDetailService);
    }
}
