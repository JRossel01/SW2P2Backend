package com.med.backend.service.auth;

import com.med.backend.dto.auth.RegisteredUser;
import com.med.backend.dto.user.SaveUser;
import com.med.backend.dto.auth.AuthenticationRequest;
import com.med.backend.dto.auth.AuthenticationResponse;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.User;
import com.med.backend.persistence.entity.security.JwtToken;
import com.med.backend.persistence.repository.DoctorRepository;
import com.med.backend.persistence.repository.PatientRepository;
import com.med.backend.persistence.repository.security.JwtTokenRepository;
import com.med.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        User user = userService.registerOneCustomer(newUser);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveUserToken(user, jwt);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public AuthenticationResponse login(@Valid AuthenticationRequest authRequest) {

        // Busca el usuario por el identificador (username o email)
        Optional<User> optionalUser = userService.findOneByIdentifier(authRequest.getIdentifier());
        if (!optionalUser.isPresent()) {
            throw new ObjectNotFoundException("User", "identifier", authRequest.getIdentifier());
        }
        User user = optionalUser.get();

        // Crea el token de autenticación
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authRequest.getPassword()
        );
        authenticationManager.authenticate(authentication);

        // Genera el JWT
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveUserToken(user, jwt);

        // Prepara la respuesta
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setJwt(jwt);
        authResponse.setRole(user.getRole().name());

        // Verifica si el usuario es un Doctor y establece el doctorId
        doctorRepository.findByUserId(user.getId()).ifPresent(doctor -> authResponse.setDoctorId(doctor.getId()));
        // Verifica si el usuario es un Paciente y establece el patientId
        patientRepository.findByUserId(user.getId()).ifPresent(patient -> authResponse.setPatientId(patient.getId()));


        return authResponse;
    }


    public boolean validate(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public User findLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Authorities: " + authentication.getAuthorities());
        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userService.findOneByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found. Username: " + username));
        }

        if (authentication != null && !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Unexpected Principal type: " + authentication.getPrincipal().getClass());
        }

        throw new RuntimeException("No authenticated user found.");
    }


    public void logout() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String jwt = jwtService.extractJwtFromRequest(request);

        if (!StringUtils.hasText(jwt)) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);

        if (token.isPresent() && token.get().isValid()) {
            token.get().setValid(false);
            jwtTokenRepository.save(token.get());
        }
    }

    private void saveUserToken(User user, String jwt) {
        JwtToken token = new JwtToken();
        token.setId(autoIncrementJwtTokenId()); // Asigna un ID único
        token.setToken(jwt);
        token.setUserId(user.getId());
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);

        jwtTokenRepository.save(token);
    }

    private int autoIncrementJwtTokenId() {
        // Obtener el último ID usado en la colección `jwt_tokens`
        int lastUsedId = jwtTokenRepository.findAll().stream()
                .map(JwtToken::getId)
                .max(Integer::compareTo)
                .orElse(0);
        // Incrementa el último ID usado
        return lastUsedId + 1;
    }
}
