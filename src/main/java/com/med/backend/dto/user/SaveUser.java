package com.med.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class SaveUser implements Serializable {
    @Size(min = 4, max = 60)
    private String name;
    // Enviar mensaje de excepcion si el username ya existe

    private String username;
    @Size(min = 8)
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 8)
    private String repeatedPassword;

    public @Size(min = 4, max = 60) String getName() {
        return name;
    }

    public void setName(@Size(min = 4, max = 60) String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 8) String password) {
        this.password = password;
    }

    public @Size(min = 8) String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(@Size(min = 8) String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
