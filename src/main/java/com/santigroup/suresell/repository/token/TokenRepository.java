package com.santigroup.suresell.repository.token;

import com.santigroup.suresell.dao.TokenModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TokenRepository {

    @Inject
    DynamoDbClient dynamoDBClient;

    private static final String TABLE_NAME = "suresell-usuario";

    public void saveToken(TokenModel token) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("tokenId", AttributeValue.builder().s(token.getTokenID()).build());
        item.put("id", AttributeValue.builder().s(token.getId()).build());
        item.put("expiration", AttributeValue.builder().n(String.valueOf(token.getExpiration().getEpochSecond())).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDBClient.putItem(request);
    }

    public TokenModel getToken(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        try {
            GetItemResponse response = dynamoDBClient.getItem(request);
            if (response.item() != null && !response.item().isEmpty()) {
                return mapToTokenModel(response.item());
            } else {
                return null; // o manejar como prefieras
            }
        } catch (DynamoDbException e) {
            // Manejar excepción
            return null;
        }
    }

    private TokenModel mapToTokenModel(Map<String, AttributeValue> item) {
        TokenModel token = new TokenModel();
        token.setId(item.get("id").s());
        token.setUsername(item.get("userId").s());
        token.setTokenID(item.get("tokenID").s());
        token.setExpiration(Instant.ofEpochSecond(Long.parseLong(item.get("expiration").n())));
        return token;
    }

}