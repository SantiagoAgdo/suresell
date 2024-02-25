package com.santigroup.suresell.controller;

import com.santigroup.suresell.dto.security.AuthRequest;
import com.santigroup.suresell.dto.security.AuthResponse;
import com.santigroup.suresell.dto.security.User;
import com.santigroup.suresell.utils.security.PBKDF2Encoder;
import com.santigroup.suresell.utils.security.TokenUtils;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/login")
public class Login {

    @Inject
    PBKDF2Encoder passwordEncoder;

    @ConfigProperty(name = "com.santigroup.suresell.jwt.duration") public Long duration;
    @ConfigProperty(name = "mp.jwt.verify.issuer") public String issuer;

    @PermitAll
    @POST
    @Path("/login") @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequest authRequest) {
        User u = User.findByUsername(authRequest.username);
        if (u != null && u.password.equals(passwordEncoder.encode(authRequest.password))) {
            try {
                return Response.ok(new AuthResponse(TokenUtils.generateToken(u.username, u.roles, duration, issuer))).build();
            } catch (Exception e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
