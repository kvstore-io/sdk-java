package io.kvstore.sdk.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class KVStoreException extends RuntimeException {

    private Map<String, Object> error;

    @SuppressWarnings("unchecked")
    public KVStoreException(String message) {
        super(message);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            error = objectMapper.readValue(message, Map.class);
        } catch (Exception e) {
            error = new HashMap<>();
            error.put("message", e.getMessage());
        }
    }

    public Map<String, Object> getError() {
        return error;
    }

}
