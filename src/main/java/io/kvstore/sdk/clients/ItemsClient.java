package io.kvstore.sdk.clients;

import io.kvstore.api.representationals.utils.SortType;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public ItemValue get(String collectionName, String itemName);

    public void put(String collectionName, String itemName, String value);

    public void delete(String collectionName, String itemName);

    public List<Item> list(String collectionName, Integer offset, Integer limit, SortType sortType);

}