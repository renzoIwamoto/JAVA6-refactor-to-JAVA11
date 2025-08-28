package com.example.legacy.legacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyBase64Test {

    @Test
    void givenString_whenEncodeAndDecode_thenStringsAreEqual() {
        // Given
        String originalString = "Hello, World!";

        // When
        String encodedString = LegacyBase64.encode(originalString.getBytes());
        byte[] decodedBytes = LegacyBase64.decode(encodedString);
        String decodedString = new String(decodedBytes);

        // Then
        assertEquals(originalString, decodedString);
    }
}
