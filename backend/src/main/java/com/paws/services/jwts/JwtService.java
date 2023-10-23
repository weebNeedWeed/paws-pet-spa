package com.paws.services.jwts;

import com.paws.entities.Customer;

public interface JwtService {
    String generateJwtToken(Customer customer);

    boolean validateJwtToken(String jwtToken);

    String extractUserNameFromJwtToken(String jwtToken);
}
