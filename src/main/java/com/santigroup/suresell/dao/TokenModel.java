package com.santigroup.suresell.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenModel {
    private String id;
    private Instant expiration;
    private String pass;
    private String tokenID;
    private String username;
}
