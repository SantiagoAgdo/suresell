package com.santigroup.suresell.controller;

import com.santigroup.suresell.service.impl.JwtTokenService;
import com.santigroup.suresell.service.impl.TokenValidationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {
    @Inject
    JwtTokenService tokenService;

    @Inject
    TokenValidationService validationService;

    @POST
    public Response login(@FormParam("username") String username,
                          @FormParam("password") String password) {
        boolean isValidUser = checkUserCredentials(username, password);

        if (isValidUser) {
            String token = tokenService.generateToken(username, password);
            return Response.ok(token).build();

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    public Response valdiarToken(String token){
        if (token != null){
            boolean isValid = validationService.validateToken(token);
            return Response.ok(isValid).build();

        }
        return Response.ok(false).build();

    }

    private boolean checkUserCredentials(String username, String password) {
        // Implementa la lógica de verificación de las credenciales
        return true; // Simplificado para el ejemplo
    }
}
