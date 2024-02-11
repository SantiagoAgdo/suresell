package com.santigroup.suresell.service.impl;


import com.santigroup.suresell.dao.TokenModel;
import com.santigroup.suresell.repository.token.TokenRepository;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipalFactory;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.Instant;

@ApplicationScoped
public class TokenValidationService {

    @Inject
    TokenRepository tokenRepository;

    @Inject
    JWTAuthContextInfo jwtAuthContextInfo;

    public boolean validateToken(String tokenString) {
        try {
            // Verifica la firma y parsea el token
            JWTCallerPrincipal parsedToken = DefaultJWTCallerPrincipalFactory.instance().parse(tokenString, jwtAuthContextInfo);

            // Verifica si el token está en la base de datos y si no ha expirado
            TokenModel tokenModel = tokenRepository.getToken(parsedToken.getTokenID());
            if (parsedToken.getExpirationTime() > 3600){
                System.out.println("si");
            }else
            {
                System.out.println("no");
            }
            if (tokenModel != null) {
                return true;
            }
        } catch (Exception e) {
            // Manejo de excepciones, por ejemplo, token inválido, firma incorrecta, etc.
            System.out.println(e);
        }
        return false;
    }
}
