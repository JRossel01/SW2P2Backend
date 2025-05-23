package com.med.backend.service.impl;

import com.med.backend.dto.user.SaveUser;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.InvalidPasswordException;
import com.med.backend.persistence.entity.User;
import com.med.backend.persistence.repository.UserRepository;
import com.med.backend.persistence.util.Role;
import com.med.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static int lastUsedId = 0;

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User registerOneCustomer(SaveUser newUser) {

        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new DuplicateResourceException(
                    "User", "username", newUser.getUsername(),
                    "The username you entered is already taken. Please choose another one"
            );
        } else if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                    "User", "email", newUser.getEmail(),
                    "The email you entered is already taken. Please choose another one"
            );
        }

        validatedPassword(newUser);

        User user = new User();
        user.setId(autoIncrement()); // Asigna el ID generado
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setRole(Role.RECEPTIONIST);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatedPassword(SaveUser newUser) {
        if (!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Password dont match");
        }

        if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Password dont match");
        }
    }

    public Optional<User> findOneByIdentifier(String identifier) {
        // Intenta encontrar al usuario por username primero
        Optional<User> user = userRepository.findByUsername(identifier);

        // Si no se encuentra por username, intenta con email
        if (!user.isPresent()) {
            user = userRepository.findByEmail(identifier);
        }

        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    private int autoIncrement() {
        // Solo inicializar lastUsedId al inicio
        if (lastUsedId == 0) {
            lastUsedId = userRepository.findAll().stream()
                    .map(User::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }

        // Incrementa el último ID usado
        lastUsedId++;
        return lastUsedId;
    }
}
