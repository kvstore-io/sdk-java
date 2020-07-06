package io.kvstore.sdk.clients;

import java.util.UUID;

/**
 * This interface describes the allowed operations on the user's storage
 */
public interface StorageClient {

    class Storage {
        private final io.kvstore.api.representationals.storages.Storage storage;

        public Storage(io.kvstore.api.representationals.storages.Storage storage) {
            this.storage = storage;
        }

        public UUID getStorageUUID() {
            return this.storage.getStorage_uuid();
        }

        public String getReferer() {
            return this.storage.getReferer();
        }

        public String toString() {
            return "Storage(storage_uuid=" + this.getStorageUUID() + ", referer=" + this.getReferer() + ")";
        }
    }

    class StorageUpdate {
        private String referer;

        public StorageUpdate setReferer(String referer) {
            this.referer = referer;
            return this;
        }

        public io.kvstore.api.representationals.storages.StorageUpdate instance() {
            return new io.kvstore.api.representationals.storages.StorageUpdate()
                .setReferer(referer);
        }
    }

    /**
     * Get the user's storage descriptor
     *
     * @return the Storage bean descriptor
     */
    Storage get();

    /**
     * Update the user's storage attributes
     *
     * @param storageUpdate the properties to update
     */
    void update(StorageUpdate storageUpdate);

}