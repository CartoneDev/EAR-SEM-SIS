package cz.cvut.fel.ear.sis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.sis.security.JWTAuthenticationProvider;
import cz.cvut.fel.ear.sis.security.JWTTokenFilter;
import cz.cvut.fel.ear.sis.service.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    final
    UserDetailService userDetailService;
    private final JWTAuthenticationProvider jwt_provider;

    private final JWTTokenFilter jwtTokenFilter;
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, UserDetailService userDetailService, @Qualifier("jwt_provider") JWTAuthenticationProvider jwt_provider, JWTTokenFilter jwtTokenFilter) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userDetailService = userDetailService;
        this.jwt_provider = jwt_provider;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    @Scope("prototype")
    @Lazy
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                        .requestMatchers(request -> request.getServletPath().startsWith("/auth")).permitAll()
                        .anyRequest().authenticated()
//                            .requestMatchers(request -> request.getContextPath().startsWith("/auth")).permitAll()
            ).formLogin(AbstractHttpConfigurer::disable
            ).logout(AbstractHttpConfigurer::disable
            ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    @Autowired public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailService userDetailService) throws Exception {
        auth.authenticationProvider(jwt_provider);
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
    final
    AuthenticationConfiguration authenticationConfiguration;
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

