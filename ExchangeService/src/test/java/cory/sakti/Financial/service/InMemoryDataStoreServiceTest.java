package cory.sakti.Financial.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryDataStoreServiceTest {
    @Test
    @DisplayName("DataStore must block writes after initialization (Constraint C)")
    void shouldFailToPutDataAfterInitialization() {
        //Arrange
        InMemoryDataStoreService store = new InMemoryDataStoreService();
        store.put("initial_key", "initial_value");

        //the seal
        store.markInitialized();

        //Assert: Attempting to put data now SHOULD fail
        assertThrows(IllegalStateException.class, () -> {
            store.put("late_key", "late_value");
        }, "Constraint C Failure: The store allowed a write after being marked initialized!");
    }
}
