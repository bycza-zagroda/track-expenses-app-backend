package pl.byczazagroda.trackexpensesappbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    private final String secret;

    public WebSecurityConfig(@Value("${jwt.secret}") String secret, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.secret = secret;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/").permitAll()
                        .antMatchers("/api/auth/login").permitAll()
                        .antMatchers("/api/users/me").authenticated()
                        .anyRequest().permitAll());
        http
                .exceptionHandling()
                .authenticationEntryPoint(new AppAuthenticationEntryPoint(objectMapper))
                .accessDeniedHandler(new AppAccessDeniedHandler(objectMapper));
        http
                .addFilterBefore(new JwtAuthorizationFilter(secret),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
