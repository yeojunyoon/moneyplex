package com.kakaopay.moneyplex.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilTest {

    @Test
    void getRandomString() {
        String randStr = RandomUtil.getRandomString(3);
        assertNotNull(randStr);
        assertEquals(randStr.length(), 3);
    }

    @Test
    void getRandLong() {
        long MAX_VALUE = 100000;
        for(int i=0; i<10000; i++){
            Long randLong = RandomUtil.getRandLong(MAX_VALUE);
            assertNotNull(randLong);
            assertEquals(randLong < MAX_VALUE, true);
        }
    }
}