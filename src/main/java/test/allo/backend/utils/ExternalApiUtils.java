package test.allo.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import test.allo.backend.exception.ExternalApiException;

public class ExternalApiUtils {

    public static void validateResponse(JsonNode response) {
        if(response == null) {
            throw new ExternalApiException(500, "No Data", "Response Null");
        }

        if (response.has("error")) {
            throw new ExternalApiException(
                    response.path("status").asInt(500),
                    response.path("error").asText("Unknown Error"),
                    response.path("message").asText("Unknown")
            );
        }
    }
}
