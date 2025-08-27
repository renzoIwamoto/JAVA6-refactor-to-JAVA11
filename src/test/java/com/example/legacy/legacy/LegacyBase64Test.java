package com.example.legacy.legacy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LegacyBase64 Tests")
class LegacyBase64Test {

    @Test
    @DisplayName("Given simple text, When encoding to Base64, Then correct encoded string is returned")
    void givenSimpleText_WhenEncodingToBase64_ThenCorrectEncodedStringIsReturned() {
        // Given
        String text = "Hello World";
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        String expectedBase64 = "SGVsbG8gV29ybGQ=";
        
        // When
        String result = LegacyBase64.encode(data);
        
        // Then
        assertThat(result).isEqualTo(expectedBase64);
    }

    @Test
    @DisplayName("Given empty byte array, When encoding, Then empty string is returned")
    void givenEmptyByteArray_WhenEncoding_ThenEmptyStringIsReturned() {
        // Given
        byte[] emptyData = new byte[0];
        
        // When
        String result = LegacyBase64.encode(emptyData);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Given special characters, When encoding, Then correct Base64 is returned")
    void givenSpecialCharacters_WhenEncoding_ThenCorrectBase64IsReturned() {
        // Given
        String text = "¡Hola! ñandú @#$%^&*()";
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        
        // When
        String encoded = LegacyBase64.encode(data);
        
        // Then
        assertThat(encoded).isNotEmpty();
        
        // Verify round-trip
        byte[] decoded = LegacyBase64.decode(encoded);
        String decodedText = new String(decoded, StandardCharsets.UTF_8);
        assertThat(decodedText).isEqualTo(text);
    }

    @Test
    @DisplayName("Given valid Base64 string, When decoding, Then original bytes are returned")
    void givenValidBase64String_WhenDecoding_ThenOriginalBytesAreReturned() {
        // Given
        String base64 = "SGVsbG8gV29ybGQ=";
        String expectedText = "Hello World";
        
        // When
        byte[] result = LegacyBase64.decode(base64);
        
        // Then
        String decodedText = new String(result, StandardCharsets.UTF_8);
        assertThat(decodedText).isEqualTo(expectedText);
    }

    @Test
    @DisplayName("Given empty Base64 string, When decoding, Then empty byte array is returned")
    void givenEmptyBase64String_WhenDecoding_ThenEmptyByteArrayIsReturned() {
        // Given
        String emptyBase64 = "";
        
        // When
        byte[] result = LegacyBase64.decode(emptyBase64);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Given invalid Base64 string, When decoding, Then IllegalArgumentException is thrown")
    void givenInvalidBase64String_WhenDecoding_ThenIllegalArgumentExceptionIsThrown() {
        // Given
        String invalidBase64 = "This is not Base64!@#$";
        
        // When & Then
        assertThatThrownBy(() -> LegacyBase64.decode(invalidBase64))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Given round-trip encoding and decoding, When processing data, Then original data is preserved")
    void givenRoundTripEncodingAndDecoding_WhenProcessingData_ThenOriginalDataIsPreserved() {
        // Given
        String originalText = "The quick brown fox jumps over the lazy dog. 1234567890!@#$%^&*()";
        byte[] originalData = originalText.getBytes(StandardCharsets.UTF_8);
        
        // When
        String encoded = LegacyBase64.encode(originalData);
        byte[] decoded = LegacyBase64.decode(encoded);
        
        // Then
        assertThat(decoded).isEqualTo(originalData);
        String decodedText = new String(decoded, StandardCharsets.UTF_8);
        assertThat(decodedText).isEqualTo(originalText);
    }

    @Test
    @DisplayName("Given binary data, When encoding and decoding, Then binary data is preserved")
    void givenBinaryData_WhenEncodingAndDecoding_ThenBinaryDataIsPreserved() {
        // Given - Create some binary data
        byte[] binaryData = new byte[256];
        for (int i = 0; i < 256; i++) {
            binaryData[i] = (byte) i;
        }
        
        // When
        String encoded = LegacyBase64.encode(binaryData);
        byte[] decoded = LegacyBase64.decode(encoded);
        
        // Then
        assertThat(decoded).isEqualTo(binaryData);
        assertThat(decoded).hasSize(256);
    }

    @Test
    @DisplayName("Given large data, When encoding and decoding, Then large data is handled correctly")
    void givenLargeData_WhenEncodingAndDecoding_ThenLargeDataIsHandledCorrectly() {
        // Given - Create 10KB of data
        StringBuilder sb = new StringBuilder();
        String pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 300; i++) {
            sb.append(pattern);
        }
        String largeText = sb.toString();
        byte[] largeData = largeText.getBytes(StandardCharsets.UTF_8);
        
        // When
        String encoded = LegacyBase64.encode(largeData);
        byte[] decoded = LegacyBase64.decode(encoded);
        
        // Then
        assertThat(decoded).isEqualTo(largeData);
        String decodedText = new String(decoded, StandardCharsets.UTF_8);
        assertThat(decodedText).isEqualTo(largeText);
        assertThat(decodedText).hasSize(largeText.length());
    }

    @Test
    @DisplayName("Given null input to encode, When encoding, Then NullPointerException is thrown")
    void givenNullInputToEncode_WhenEncoding_ThenNullPointerExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> LegacyBase64.encode(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Given null input to decode, When decoding, Then NullPointerException is thrown")
    void givenNullInputToDecode_WhenDecoding_ThenNullPointerExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> LegacyBase64.decode(null))
            .isInstanceOf(NullPointerException.class);
    }
}