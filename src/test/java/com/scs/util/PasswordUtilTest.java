package com.scs.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void hashMatchesKnownSha256() {
        assertEquals(
                "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9",
                PasswordUtil.hash("admin123"));
        assertEquals(
                "4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b",
                PasswordUtil.hash("citizen123"));
        assertEquals(
                "118b8d35a17bcf2c7d2d790509e12308dc6332c5d234f0098d2d6be6700bebb1",
                PasswordUtil.hash("officer123"));
    }

    @Test
    public void verifyRoundTrip() {
        String h = PasswordUtil.hash("MySecret!42");
        assertTrue(PasswordUtil.verify("MySecret!42", h));
        assertFalse(PasswordUtil.verify("MySecret!43", h));
        assertFalse(PasswordUtil.verify(null, h));
        assertFalse(PasswordUtil.verify("x", null));
    }
}
