package pl.byczazagroda.trackexpensesappbackend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.byczazagroda.trackexpensesappbackend.service.CustomUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final UserDetailsService userDetailsService;

    private final String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            try {
                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX, ""));
                String userName = decodedJWT.getSubject();

                if (userName != null) {
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userName);
                    return new UsernamePasswordAuthenticationToken(userDetails.getId(), null, userDetails.getAuthorities());
                }
            } catch (JWTVerificationException e) {
                throw new AuthenticationServiceException("Failed to verify token: " + e.getMessage(), e);
            }
        }
        return null;
    }

}
