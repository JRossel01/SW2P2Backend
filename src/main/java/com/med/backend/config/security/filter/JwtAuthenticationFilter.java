package com.med.backend.config.security.filter;

import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.User;
import com.med.backend.persistence.entity.security.JwtToken;
import com.med.backend.persistence.repository.security.JwtTokenRepository;
import com.med.backend.service.UserService;
import com.med.backend.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JOIN ON THE FILTER JwtAuthenticationFilter");
        // 1. Obtain header named "Authorization"
        // 2. Extract token JWT from header
        String jwt = jwtService.extractJwtFromRequest(request);
        System.out.println("Extracted JWT: " + jwt);

        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            System.out.println("No JWT found in request header");
            return;
        }

        // 2.1. Obtain token not expired and valid from the database
        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);
        boolean isValid = validateToken(token);

        if (!isValid) {
            System.out.println("JWT is not valid or not found in the system");
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Obtain the subject/username from the token and validate its format, signature, and expiration
        String username = jwtService.extractUsername(jwt);
        System.out.println("JWT belongs to user: " + username);

        // 4. Set object Authentication in SecurityContextHolder
        User user = userService.findOneByUsername(username)
                .orElseThrow( () -> new RuntimeException("User not found. Username: " + username));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Continue with the filter chain

        filterChain.doFilter(request, response);
    }

    private boolean validateToken(Optional<JwtToken> optionalJwtToken) {
        if (!optionalJwtToken.isPresent()) {
            System.out.println("El token no existe o no fue generado en nuestro sistema");
            return false;
        }

        JwtToken token = optionalJwtToken.get();
        Date now = new Date(System.currentTimeMillis());
        System.out.println("Token expiration: " + token.getExpiration());
        System.out.println("Current time: " + now);
        System.out.println("Is token valid? " + token.isValid());
        System.out.println("Has token expired? " + token.getExpiration().after(now));

        boolean isTokenValid = token.isValid();
        boolean isNotExpired = token.getExpiration().after(now);

        if (!isTokenValid) {
            System.out.println("El token no es válido.");
        }

        if (!isNotExpired) {
            System.out.println("El token ha expirado.");
        }

        boolean isValid = isTokenValid && isNotExpired;

        if (!isValid) {
            updateTokenStatus(token);
        }

        return isValid;
    }


    private void updateTokenStatus(JwtToken token) {
        token.setValid(false);
        jwtTokenRepository.save(token);
    }
}
