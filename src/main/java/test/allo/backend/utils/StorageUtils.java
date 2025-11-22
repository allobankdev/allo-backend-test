package test.allo.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class StorageUtils {

    public static boolean isValidData(JsonNode response) {
        return response != null && response.isObject() && !response.has("error");
    }
}
