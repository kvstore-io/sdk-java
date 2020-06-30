package io.kvstore.sdk.clients.impls;

import io.kvstore.sdk.KVStore;
import io.kvstore.sdk.clients.StorageClient;

public class StorageClientImpl implements StorageClient {

    private final KVStore kvStore;

    public StorageClientImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    public Storage get() {
        return new Storage(kvStore.get("/storage", KVStore.CONTENT_TYPE_JSON, io.kvstore.api.representationals.storages.Storage.class));
    }

    public void update(StorageUpdate storageUpdate) {
        kvStore.put("/storage", storageUpdate.instance());
    }

}