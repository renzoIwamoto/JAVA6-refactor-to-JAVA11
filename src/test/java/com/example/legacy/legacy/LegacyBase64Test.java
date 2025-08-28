package com.example.legacy.legacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyBase64Test {
    @Test
    void givenText_whenEncodeDecode_thenRoundtrip() throws Exception {
        String text = "Hola-π🚀";
        String b64 = LegacyBase64.encode(text.getBytes("UTF-8"));
        byte[] back = LegacyBase64.decode(b64);
        assertEquals(text, new String(back, "UTF-8"));
    }
}

