package com.med.backend.controller;

import com.med.backend.dto.auth.LogoutResponse;
import com.med.backend.dto.auth.AuthenticationRequest;
import com.med.backend.dto.auth.AuthenticationResponse;
import com.med.backend.dto.user.UserDTO;
import com.med.backend.persistence.entity.User;
import com.med.backend.service.auth.AuthenticationService;
import com.med.backend.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @PreAuthorize("permitAll()")
    @MutationMapping(name = "authenticate")
    public AuthenticationResponse authenticate(@Argument("input") AuthenticationRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest);
    }

    @PreAuthorize("permitAll()")
    @QueryMapping(name = "validateToken")
    public Boolean validateToken() {
        // Retrieve the HttpServletRequest from the current request context
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            System.out.println("No request context available");
            return false;
        }

        HttpServletRequest request = attributes.getRequest();
        String jwt = jwtService.extractJwtFromRequest(request);
        if (!StringUtils.hasText(jwt)) {
            System.out.println("No JWT found in the request header");
            return false;
        }
        return authenticationService.validate(jwt);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "logout")
    public LogoutResponse logout() {
        authenticationService.logout();
        return new LogoutResponse("Logout successful");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','DOCTOR','RECEPTIONIST','PATIENT')")
    @QueryMapping(name = "findMyProfile")
    public User findMyProfile() {
        User user = authenticationService.findLoggedInUser();

        List<String> authoritiesList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        user.setTransientAuthorities(authoritiesList);

        return user;
    }
}
