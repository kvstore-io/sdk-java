package io.kvstore.sdk.clients.impls;

import io.kvstore.api.representationals.utils.SortType;
import io.kvstore.sdk.KVStore;
import io.kvstore.sdk.clients.ItemsClient;

import java.util.ArrayList;
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
    @SuppressWarnings("unchecked")
    public List<Item> list(String collectionName, Integer offset, Integer limit, SortType sortType) {
        String url = "/collections/" + collectionName + "/items";

        if (offset != null || limit != null || sortType != null) {
            List<String> params = new ArrayList<>();

            if (offset != null) {
                params.add("offset=" + offset);
            }

            if (limit != null) {
                params.add("limit=" + limit);
            }

            if (sortType != null) {
                params.add("sort=" + sortType.name());
            }

            url += "?" + String.join("&", params);
        }

        List<Map<String, Object>> list = kvStore.get(url, KVStore.CONTENT_TYPE_JSON, List.class);
        return list.stream().map(Item::instance).collect(Collectors.toList());
    }

}