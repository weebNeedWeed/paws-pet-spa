package com.paws.application.services.jwts;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.paws.persistence.entities.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService{
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry-days}")
    private int expiryDays;

    @Override
    public String generateJwtToken(Customer customer) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        int daysInMilis = expiryDays * 24 * 60 * 60 * 1000;

        Date current = new Date();
        Date expiredAt = new Date(current.getTime() + daysInMilis);

        String jwtToken = JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withIssuer("Paws")
                .withAudience("Paws")
                .withIssuedAt(current)
                .withExpiresAt(expiredAt)
                .withSubject(customer.getUsername())
                .withClaim("name", customer.getFullName())
                .withClaim("email", customer.getEmail())
                .withClaim("subject_id", customer.getId())
                .sign(algorithm);

        return jwtToken;
    }

    @Override
    public boolean validateJwtToken(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            verifier.verify(jwtToken);

            return true;
        } catch (JWTVerificationException ex) {
            throw new AuthenticationCredentialsNotFoundException("Jwt token is invalid or expired.");
        }
    }

    @Override
    public String extractUserNameFromJwtToken(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT jwt = JWT.require(algorithm)
                .build()
                .verify(jwtToken);
        String username = jwt.getSubject();

        return username;
    }
}
