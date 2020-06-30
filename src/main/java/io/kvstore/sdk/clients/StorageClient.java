package io.kvstore.sdk.clients;

import java.util.UUID;

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

    Storage get();

    void update(StorageUpdate storageUpdate);

}