package com.example.legacy.legacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyBase64Test {

    @Test
    void givenValidByteArray_whenEncoding_thenReturnsBase64String() {
        // Given
        var input = "Hello World".getBytes();

        // When
        var result = LegacyBase64.encode(input);

        // Then
        assertNotNull(result);
        assertEquals("SGVsbG8gV29ybGQ=", result);
    }

    @Test
    void givenEmptyByteArray_whenEncoding_thenReturnsEmptyBase64String() {
        // Given
        var input = new byte[0];

        // When
        var result = LegacyBase64.encode(input);

        // Then
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void givenValidBase64String_whenDecoding_thenReturnsOriginalBytes() {
        // Given
        var input = "SGVsbG8gV29ybGQ=";

        // When
        var result = LegacyBase64.decode(input);

        // Then
        assertNotNull(result);
        assertEquals("Hello World", new String(result));
    }

    @Test
    void givenEmptyBase64String_whenDecoding_thenReturnsEmptyByteArray() {
        // Given
        var input = "";

        // When
        var result = LegacyBase64.decode(input);

        // Then
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void givenByteArray_whenEncodingAndDecoding_thenReturnsOriginalData() {
        // Given
        var originalData = "This is a test message with special chars: áéíóú ñ @#$%".getBytes();

        // When
        var encoded = LegacyBase64.encode(originalData);
        var decoded = LegacyBase64.decode(encoded);

        // Then
        assertArrayEquals(originalData, decoded);
        assertEquals(new String(originalData), new String(decoded));
    }

    @Test
    void givenBinaryData_whenEncodingAndDecoding_thenReturnsOriginalData() {
        // Given
        var originalData = new byte[]{0, 1, 2, 3, -1, -2, -3, 127, -128};

        // When
        var encoded = LegacyBase64.encode(originalData);
        var decoded = LegacyBase64.decode(encoded);

        // Then
        assertArrayEquals(originalData, decoded);
    }

    @Test
    void givenLargeByteArray_whenEncodingAndDecoding_thenReturnsOriginalData() {
        // Given
        var originalData = new byte[1000];
        for (int i = 0; i < originalData.length; i++) {
            originalData[i] = (byte) (i % 256);
        }

        // When
        var encoded = LegacyBase64.encode(originalData);
        var decoded = LegacyBase64.decode(encoded);

        // Then
        assertArrayEquals(originalData, decoded);
    }

    @Test
    void givenInvalidBase64String_whenDecoding_thenThrowsException() {
        // Given
        var invalidBase64 = "This is not base64!";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            LegacyBase64.decode(invalidBase64);
        });
    }
}