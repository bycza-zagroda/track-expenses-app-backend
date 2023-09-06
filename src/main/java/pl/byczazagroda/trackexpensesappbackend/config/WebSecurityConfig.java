package pl.byczazagroda.trackexpensesappbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secret;

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
                        .antMatchers("/api/auth/refresh").permitAll()
                        .antMatchers(
                                "/api/users/me",
                                "/api/transactions/**",
                                "/api/categories/**",
                                "/api/wallets/**",
                                "/api/auth/logout"
                        )
                        .authenticated()
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
