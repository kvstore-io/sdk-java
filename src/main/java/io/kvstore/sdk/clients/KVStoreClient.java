package io.kvstore.sdk.clients;

/**
 * This instance is obtained from the KVStore instance methods and gives access to different handlers on KVStore
 */
public interface KVStoreClient {

    StorageClient storageClient();

    CollectionsClient collectionsClient();

    ItemsClient itemsClient();

}
