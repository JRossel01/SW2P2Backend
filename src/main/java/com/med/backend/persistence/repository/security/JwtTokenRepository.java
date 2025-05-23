package com.med.backend.persistence.repository.security;

import com.med.backend.persistence.entity.security.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JwtTokenRepository extends MongoRepository<JwtToken, Integer> {
    Optional<JwtToken> findByToken(String jwt);
}
