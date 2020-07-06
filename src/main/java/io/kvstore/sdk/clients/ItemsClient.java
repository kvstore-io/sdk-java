package io.kvstore.sdk.clients;

import io.kvstore.api.representationals.utils.SortType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This interface describes the operations on storage values
 */
public interface ItemsClient {

    class Item {
        private final io.kvstore.api.representationals.items.Item item;

        public Item(io.kvstore.api.representationals.items.Item item) {
            this.item = item;
        }

        public String getKey() {
            return this.item.getKey();
        }

        public String getValue() {
            return this.item.getValue();
        }

        public Date getCreatedAt() {
            return new Date((long) (this.item.getCreated_at() * 1000));
        }

        public Date getUpdatedAt() {
            return new Date((long) (this.item.getUpdated_at() * 1000));
        }

        public String toString() {
            return "Item(key=" + this.getKey() + ", value=" + this.getValue() + ", created_at=" + this.getCreatedAt() + ", updated_at=" + this.getUpdatedAt() + ")";
        }

        public static Item instance(Map<String, Object> map) {
            return new Item(new io.kvstore.api.representationals.items.Item()
                .setKey((String) map.get("key"))
                .setValue((String) map.get("value"))
                .setUpdated_at((Double) map.get("updated_at"))
                .setCreated_at((Double) map.get("created_at")));
        }

    }

    class ItemValue {
        private final io.kvstore.api.representationals.items.ItemValue itemValue;

        public ItemValue(io.kvstore.api.representationals.items.ItemValue item) {
            this.itemValue = item;
        }

        public String getValue() {
            return this.itemValue.getValue();
        }

        public Date getCreatedAt() {
            return new Date((long) (this.itemValue.getCreated_at() * 1000));
        }

        public Date getUpdatedAt() {
            return new Date((long) (this.itemValue.getUpdated_at() * 1000));
        }

        public String toString() {
            return "Item(value=" + this.getValue() + ", created_at=" + this.getCreatedAt() + ", updated_at=" + this.getUpdatedAt() + ")";
        }
    }

    /**
     * Get the value for the given collection and key
     * @param collectionName the name of the collection
     * @param itemName the key value
     * @return the stored value
     */
    ItemValue get(String collectionName, String itemName);

    /**
     * Store a value
     * @param collectionName the name of the collection we're putting the value inside
     * @param itemName the name of the key
     * @param value the value to be stored
     */
    void put(String collectionName, String itemName, String value);

    /**
     * Delete a stored value
     * @param collectionName the name of the collection
     * @param itemName the key value to be deleted
     */
    void delete(String collectionName, String itemName);

    /**
     * Get the list of stored values inside a collection
     * @param collectionName the name of the collection
     * @param offset the offset to start from (default 0)
     * @param limit the maximum number of items to fetch
     * @param sortType the type of sorting (ascending or descending by value creation date)
     * @return the list of found items
     */
    List<Item> list(String collectionName, Integer offset, Integer limit, SortType sortType);

}