package io.kvstore.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kvstore.sdk.clients.CollectionsClient;
import io.kvstore.sdk.clients.ItemsClient;
import io.kvstore.sdk.clients.StorageClient;
import io.kvstore.sdk.clients.impls.CollectionsClientImpl;
import io.kvstore.sdk.clients.impls.ItemsClientImpl;
import io.kvstore.sdk.clients.impls.StorageClientImpl;
import io.kvstore.sdk.exceptions.KVStoreException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class KVStore {
    public enum ENV {PRODUCTION, STAGING, DEVELOPMENT}

    private enum HTTP_METHOD {GET, PUT, DELETE, POST}

    public final static String CONTENT_TYPE_JSON = "application/json";
    public final static String CONTENT_TYPE_TEXT = "text/plain";

    private final String APIToken;
    private final ENV env;

    private static final String productionBaseURL = "https://api.kvstore.io";
    private static final String stagingBaseURL = "https://staging-api.kvstore.io";
    private static final String developmentBaseURL = "http://localhost:8080";

    private final ObjectMapper objectMapper;

    private KVStore(String APIToken, ENV env) {
        this.APIToken = APIToken;
        this.env = env;

        objectMapper = new ObjectMapper();
    }

    public static KVStore instance(String APIToken) {
        return new KVStore(APIToken, ENV.PRODUCTION);
    }

    public static KVStore instance(String APIToken, ENV env) {
        return new KVStore(APIToken, env);
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
        String s = this.doRequest(endpoint, HTTP_METHOD.GET, false, true, contentType, null);
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
        this.doRequest(endpoint, HTTP_METHOD.PUT, true, true, contentType, payload);
    }

    public void delete(String endpoint) {
        this.doRequest(endpoint, HTTP_METHOD.DELETE, false, true, null, null);
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

        return this.doRequest(endpoint, HTTP_METHOD.POST, true, true, contentType, payload);
    }

    private String doRequest(String endpoint, HTTP_METHOD httpMethod, boolean doOut, boolean doIn, String requestContentType, String payload) {
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
            connection.setDoInput(doIn);

            if (payload != null) {
                try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
                    writer.write(payload.getBytes());
                    writer.flush();
                }
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                throw new KVStoreException(response.toString());
            }

        } catch (KVStoreException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getBaseURL() {
        switch (env) {
            case STAGING:
                return stagingBaseURL;
            case PRODUCTION:
                return productionBaseURL;
            default:
                return developmentBaseURL;
        }
    }

}