package io.kvstore.sdk.clients;

public interface KVStoreClient {

    StorageClient storageClient();

    CollectionsClient collectionsClient();

    ItemsClient itemsClient();

}
