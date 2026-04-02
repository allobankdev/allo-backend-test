
package com.allo.test.unit;

import com.allo.test.util.SpreadUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SpreadUtilTest {

    @Test
    void testSpread(){
        SpreadUtil util = new SpreadUtil();
        ReflectionTestUtils.setField(util,"username","haidir");
        double result = util.calculateSpread();
        assertTrue(result >= 0);
    }
}
