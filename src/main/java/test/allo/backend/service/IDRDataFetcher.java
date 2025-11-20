package test.allo.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public interface IDRDataFetcher {
    JsonNode fetchData();
}
