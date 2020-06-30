package io.kvstore.sdk.clients;

import io.kvstore.api.representationals.collections.CollectionsResult;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public interface CollectionsClient {

    class CollectionInfoCompact {
        private final io.kvstore.api.representationals.collections.CollectionInfoCompact collectionInfoCompact;

        public Integer getItems() {
            return collectionInfoCompact.getItems();
        }

        public Date getCreatedAt() {
            return new Date((long) (this.collectionInfoCompact.getCreated_at() * 1000));
        }

        public Date getUpdatedAt() {
            return new Date((long) (this.collectionInfoCompact.getUpdated_at() * 1000));
        }

        public CollectionInfoCompact(io.kvstore.api.representationals.collections.CollectionInfoCompact collectionInfoCompact) {
            this.collectionInfoCompact = collectionInfoCompact;
        }

        public String toString() {
            return "CollectionCompact(items=" + this.getItems() + ", created_at=" + this.getCreatedAt() + ", updated_at=" + this.getUpdatedAt() + ")";
        }
    }

    class CollectionInfo {
        private final io.kvstore.api.representationals.collections.CollectionInfo collectionInfo;

        public CollectionInfo(io.kvstore.api.representationals.collections.CollectionInfo collectionInfo) {
            this.collectionInfo = collectionInfo;
        }

        public Integer getItems() {
            return collectionInfo.getItems();
        }

        public Boolean getPublicWrite() {
            return collectionInfo.getPublic_write();
        }

        public Boolean getPublicRead() {
            return collectionInfo.getPublic_read();
        }

        public String getWebhook() {
            return this.collectionInfo.getWebhook();
        }

        public Date getCreatedAt() {
            return new Date((long) (this.collectionInfo.getCreated_at() * 1000));
        }

        public Date getUpdatedAt() {
            return new Date((long) (this.collectionInfo.getUpdated_at() * 1000));
        }

        public String toString() {
            return "Collection(items=" + this.getItems() +
                ", publicWrite=" + this.getPublicWrite() +
                ", publicRead=" + this.getPublicRead() +
                ", webhook=" + this.getWebhook() +
                ", createdAt=" + this.getCreatedAt() +
                ", updatedAt=" + this.getUpdatedAt() + ")";
        }
    }

    class CollectionsList {
        private final CollectionsResult collectionsResult;

        public Integer getTotalCollections() {
            return collectionsResult.getTotal_collections();
        }

        public Map<String, CollectionInfoCompact> getCollections() {
            return collectionsResult.getCollections().entrySet().stream().
                collect(Collectors.toMap(Map.Entry::getKey, e -> new CollectionInfoCompact(e.getValue())));
        }

        public CollectionsList(CollectionsResult collectionsResult) {
            this.collectionsResult = collectionsResult;
        }
    }

    class UpdateCollection {
        private Boolean publicWrite;
        private Boolean publicRead;
        private String emailNotification;
        private String webhook;
        private String webhookSecret;

        public Boolean getPublicWrite() {
            return publicWrite;
        }

        public Boolean getPublicRead() {
            return publicRead;
        }

        public String getEmailNotification() {
            return emailNotification;
        }

        public String getWebhook() {
            return webhook;
        }

        public String getWebhookSecret() {
            return webhookSecret;
        }

        public UpdateCollection setEmailNotification(String emailNotification) {
            this.emailNotification = emailNotification;
            return this;
        }

        public UpdateCollection setPublicWrite(Boolean publicWrite) {
            this.publicWrite = publicWrite;
            return this;
        }

        public UpdateCollection setPublicRead(Boolean publicRead) {
            this.publicRead = publicRead;
            return this;
        }

        public UpdateCollection setWebhook(String webhook) {
            this.webhook = webhook;
            return this;
        }

        public UpdateCollection setWebhookSecret(String webhookSecret) {
            this.webhookSecret = webhookSecret;
            return this;
        }

        public io.kvstore.api.representationals.collections.UpdateCollection instance() {
            return new io.kvstore.api.representationals.collections.UpdateCollection()
                .setEmail_notification(emailNotification)
                .setPublic_read(publicRead)
                .setPublic_write(publicWrite)
                .setWebhook(webhook)
                .setWebhook_secret(webhookSecret);
        }
    }

    CollectionsList list();

    CollectionInfo get(String collectionName);

    void create(String collectionName);

    void update(String collectionName, UpdateCollection updateCollection);

    void delete(String collectionName);

}