package com.example.legacy.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InstantAdapter Tests")
class InstantAdapterTest {

    private final InstantAdapter adapter = new InstantAdapter();

    @Test
    @DisplayName("Given valid ISO instant string, When unmarshalling, Then Instant is returned")
    void givenValidIsoInstantString_WhenUnmarshalling_ThenInstantIsReturned() throws Exception {
        // Given
        String isoString = "2024-01-15T10:30:00Z";
        
        // When
        Instant result = adapter.unmarshal(isoString);
        
        // Then
        assertThat(result).isEqualTo(Instant.parse(isoString));
    }

    @Test
    @DisplayName("Given null string, When unmarshalling, Then null is returned")
    void givenNullString_WhenUnmarshalling_ThenNullIsReturned() throws Exception {
        // When
        Instant result = adapter.unmarshal(null);
        
        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Given invalid instant string, When unmarshalling, Then exception is thrown")
    void givenInvalidInstantString_WhenUnmarshalling_ThenExceptionIsThrown() {
        // Given
        String invalidString = "not-a-valid-instant";
        
        // When & Then
        assertThatThrownBy(() -> adapter.unmarshal(invalidString))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Given valid Instant, When marshalling, Then ISO string is returned")
    void givenValidInstant_WhenMarshalling_ThenIsoStringIsReturned() throws Exception {
        // Given
        Instant instant = Instant.parse("2024-02-01T14:15:30Z");
        
        // When
        String result = adapter.marshal(instant);
        
        // Then
        assertThat(result).isEqualTo("2024-02-01T14:15:30Z");
    }

    @Test
    @DisplayName("Given null Instant, When marshalling, Then null is returned")
    void givenNullInstant_WhenMarshalling_ThenNullIsReturned() throws Exception {
        // When
        String result = adapter.marshal(null);
        
        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Given round-trip marshalling and unmarshalling, When processing Instant, Then Instant is preserved")
    void givenRoundTripMarshallingAndUnmarshalling_WhenProcessingInstant_ThenInstantIsPreserved() throws Exception {
        // Given
        Instant originalInstant = Instant.parse("2024-03-10T09:45:12.123456Z");
        
        // When
        String marshalled = adapter.marshal(originalInstant);
        Instant unmarshalled = adapter.unmarshal(marshalled);
        
        // Then
        assertThat(unmarshalled).isEqualTo(originalInstant);
    }

    @Test
    @DisplayName("Given Instant at epoch, When marshalling, Then epoch string is returned")
    void givenInstantAtEpoch_WhenMarshalling_ThenEpochStringIsReturned() throws Exception {
        // Given
        Instant epochInstant = Instant.EPOCH;
        
        // When
        String result = adapter.marshal(epochInstant);
        
        // Then
        assertThat(result).isEqualTo("1970-01-01T00:00:00Z");
    }

    @Test
    @DisplayName("Given far future Instant, When marshalling, Then correct string is returned")
    void givenFarFutureInstant_WhenMarshalling_ThenCorrectStringIsReturned() throws Exception {
        // Given
        Instant futureInstant = Instant.parse("2099-12-31T23:59:59Z");
        
        // When
        String result = adapter.marshal(futureInstant);
        
        // Then
        assertThat(result).isEqualTo("2099-12-31T23:59:59Z");
    }
}