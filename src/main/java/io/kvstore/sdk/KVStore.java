package io.kvstore.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kvstore.sdk.clients.CollectionsClient;
import io.kvstore.sdk.clients.ItemsClient;
import io.kvstore.sdk.clients.KVStoreClient;
import io.kvstore.sdk.clients.StorageClient;
import io.kvstore.sdk.clients.impls.CollectionsClientImpl;
import io.kvstore.sdk.clients.impls.ItemsClientImpl;
import io.kvstore.sdk.clients.impls.StorageClientImpl;
import io.kvstore.sdk.exceptions.KVStoreException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class KVStore implements KVStoreClient {
    public enum ENV {PRODUCTION, STAGING, DEVELOPMENT}

    private enum HTTP_METHOD {GET, PUT, DELETE, POST}

    public final static String CONTENT_TYPE_JSON = "application/json";
    public final static String CONTENT_TYPE_TEXT = "text/plain";

    private final String APIToken;
    private final ENV env;
    private final Integer port;

    private static final String productionBaseURL = "https://api.kvstore.io";
    private static final String stagingBaseURL = "https://staging-api.kvstore.io";
    private static final String developmentBaseURL = "http://localhost";


    private final ObjectMapper objectMapper;

    private KVStore(String APIToken, ENV env, Integer port) {
        this.APIToken = APIToken;
        this.env = env;
        this.port = port;

        objectMapper = new ObjectMapper();
    }

    public static KVStoreClient instance(String APIToken) {
        return new KVStore(APIToken, ENV.PRODUCTION, null);
    }

    public static KVStoreClient instance(String APIToken, ENV env) {
        return new KVStore(APIToken, env, null);
    }

    public static KVStoreClient instance(String APIToken, int port) {
        return new KVStore(APIToken, ENV.DEVELOPMENT, port);
    }

    public StorageClient storageClient() {
        return new StorageClientImpl(this);
    }

    public CollectionsClient collectionsClient() {
        return new CollectionsClientImpl(this);
    }

    public ItemsClient itemsClient() {
        return new ItemsClientImpl(this);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String endpoint, String contentType, Class<T> pojoClass) {
        String s = this.doRequest(endpoint, HTTP_METHOD.GET, false, contentType, null);
        if (pojoClass != null) {
            try {
                return objectMapper.readValue(s, pojoClass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T) s;
    }

    public void put(String endpoint, Object object) {
        String payload;
        String contentType;
        if (object != null && !(object.getClass().isAssignableFrom(String.class))) {
            try {
                payload = objectMapper.writeValueAsString(object);
                contentType = CONTENT_TYPE_JSON;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            payload = (String) object;
            contentType = CONTENT_TYPE_TEXT;
        }
        this.doRequest(endpoint, HTTP_METHOD.PUT, true, contentType, payload);
    }

    public void delete(String endpoint) {
        this.doRequest(endpoint, HTTP_METHOD.DELETE, false, null, null);
    }

    public String post(String endpoint, Object object) {
        String payload = null;
        String contentType = null;
        if (object != null) {
            try {
                payload = objectMapper.writeValueAsString(object);
                contentType = CONTENT_TYPE_JSON;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return this.doRequest(endpoint, HTTP_METHOD.POST, true, contentType, payload);
    }

    private String doRequest(String endpoint, HTTP_METHOD httpMethod, boolean doOut, String requestContentType, String payload) {
        try {
            URL url = new URL(getBaseURL() + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.name());
            connection.setRequestProperty("accept", "*/*");
            if (requestContentType != null) {
                connection.setRequestProperty("Content-Type", requestContentType);
            }
            connection.setRequestProperty("kvstoreio_api_key", APIToken);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(doOut);

            if (payload != null) {
                try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
                    writer.write(payload.getBytes());
                    writer.flush();
                }
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readInputStream(connection.getInputStream());
            } else {
                throw new KVStoreException(readInputStream(connection.getErrorStream()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private String getBaseURL() {
        switch (env) {
            case STAGING:
                return stagingBaseURL;
            case PRODUCTION:
                return productionBaseURL;
            default:
                return developmentBaseURL + ":" + ((port != null) ? port : "8080");
        }
    }

}