package io.kvstore.sdk.clients.impls;

import io.kvstore.api.representationals.collections.CollectionsResult;
import io.kvstore.sdk.KVStore;
import io.kvstore.sdk.clients.CollectionsClient;

import java.util.HashMap;
import java.util.Map;

public class CollectionsClientImpl implements CollectionsClient {

    private final KVStore kvStore;

    public CollectionsClientImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    @Override
    public CollectionsList list() {
        return new CollectionsList(kvStore.get("/collections", KVStore.CONTENT_TYPE_JSON, CollectionsResult.class));
    }

    @Override
    public CollectionInfo get(String collectionName) {
        return new CollectionInfo(kvStore.get("/collections/" + collectionName, KVStore.CONTENT_TYPE_JSON, io.kvstore.api.representationals.collections.CollectionInfo.class));
    }

    @Override
    public void create(String collectionName) {
        Map<String, Object> map = new HashMap<>();
        map.put("collection", collectionName);
        kvStore.post("/collections", map);
    }

    @Override
    public void update(String collectionName, UpdateCollection updateCollection) {
        kvStore.put("/collections/" + collectionName, updateCollection.instance());
    }

    @Override
    public void delete(String collectionName) {
        kvStore.delete("/collections/" + collectionName);
    }

}