package com.med.backend.dto.auth;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
    private String identifier; // Cambiamos el nombre a "identifier"
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
