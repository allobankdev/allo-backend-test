
package com.allo.finance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpreadTest {

    @Test
    void testSpread(){
        String user = "haidir";
        int sum = user.chars().sum();
        double spread = (sum % 1000) / 100000.0;
        assertTrue(spread >= 0);
    }
}
