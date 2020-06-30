package io.kvstore.sdk.clients.impls;

import io.kvstore.api.representationals.utils.SortType;
import io.kvstore.sdk.KVStore;
import io.kvstore.sdk.clients.ItemsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemsClientImpl implements ItemsClient {

    private final KVStore kvStore;

    public ItemsClientImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    @Override
    public ItemValue get(String collectionName, String itemName) {
        return new ItemsClient.ItemValue(kvStore.get("/collections/" + collectionName + "/items/" + itemName, KVStore.CONTENT_TYPE_JSON, io.kvstore.api.representationals.items.ItemValue.class));
    }

    @Override
    public void put(String collectionName, String itemName, String value) {
        kvStore.put("/collections/" + collectionName + "/items/" + itemName, value);
    }

    @Override
    public void delete(String collectionName, String itemName) {
        kvStore.delete("/collections/" + collectionName + "/items/" + itemName);
    }

    @Override
    public List<Item> list(String collectionName, Integer offset, Integer limit, SortType sortType) {
        List<Map<String, Object>> list = kvStore.get("/collections/" + collectionName + "/items", KVStore.CONTENT_TYPE_JSON, List.class);
        return list.stream().map(Item::instance).collect(Collectors.toList());
    }

}