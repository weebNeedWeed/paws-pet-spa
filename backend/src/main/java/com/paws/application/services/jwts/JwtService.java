package com.paws.application.services.jwts;

import com.paws.persistence.entities.Customer;

public interface JwtService {
    String generateJwtToken(Customer customer);

    boolean validateJwtToken(String jwtToken);

    String extractUserNameFromJwtToken(String jwtToken);
}
