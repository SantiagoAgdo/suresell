package com.santigroup.suresell.service.impl;

import com.santigroup.suresell.dao.TokenModel;
import com.santigroup.suresell.repository.token.TokenRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class JwtTokenService {

    @Inject
    TokenRepository tokenRepository;

    private static final long TOKEN_DURATION = 3600;

    public String generateToken(String username, String pass) {
        String jwt = Jwt.issuer("https://your-issuer-here.com")
                .upn(username)
                .expiresIn(Duration.ofSeconds(TOKEN_DURATION))
                // Aquí puedes añadir más claims si lo necesitas
                .sign();

        TokenModel token = new TokenModel("312312", calculateExpirationTime(), pass, jwt, username);

        tokenRepository.saveToken(token);

        return jwt;
    }





    public Instant calculateExpirationTime() {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(30, ChronoUnit.MINUTES);
        return expirationTime;
    }

}
