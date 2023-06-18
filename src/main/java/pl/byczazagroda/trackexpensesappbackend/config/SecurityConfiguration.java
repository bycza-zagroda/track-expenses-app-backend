package pl.byczazagroda.trackexpensesappbackend.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
             httpSecurity.csrf().disable()
                     .authorizeHttpRequests()
                     .requestMatchers("")
                     .permitAll()
                     .anyRequest()
                     .authenticated()
                     .and()
                     .sessionManagement()
                     .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                     .and()
                     .authenticationProvider(authenticationProvider)
                     .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                     .
              return  httpSecurity.build();
    }
}
