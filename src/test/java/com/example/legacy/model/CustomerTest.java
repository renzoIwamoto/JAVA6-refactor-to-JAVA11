package com.example.legacy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Customer Model Tests")
class CustomerTest {

    @Test
    @DisplayName("Given valid parameters, When creating Customer, Then all fields are set correctly")
    void givenValidParameters_WhenCreatingCustomer_ThenAllFieldsAreSetCorrectly() {
        // Given
        String id = "customer-123";
        String name = "John Doe";
        String email = "john.doe@example.com";
        
        // When
        Customer customer = new Customer(id, name, email);
        
        // Then
        assertThat(customer.getId()).isEqualTo(id);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Given customer is immutable, When accessing getters, Then same values are returned")
    void givenCustomerIsImmutable_WhenAccessingGetters_ThenSameValuesAreReturned() {
        // Given
        String id = "customer-456";
        String name = "Jane Smith";
        String email = "jane.smith@company.org";
        Customer customer = new Customer(id, name, email);
        
        // When & Then - Multiple calls should return same values
        for (int i = 0; i < 3; i++) {
            assertThat(customer.getId()).isEqualTo(id);
            assertThat(customer.getName()).isEqualTo(name);
            assertThat(customer.getEmail()).isEqualTo(email);
        }
    }

    @Test
    @DisplayName("Given null values, When creating Customer, Then null values are stored")
    void givenNullValues_WhenCreatingCustomer_ThenNullValuesAreStored() {
        // When
        Customer customer = new Customer(null, null, null);
        
        // Then
        assertThat(customer.getId()).isNull();
        assertThat(customer.getName()).isNull();
        assertThat(customer.getEmail()).isNull();
    }

    @Test
    @DisplayName("Given empty strings, When creating Customer, Then empty strings are stored")
    void givenEmptyStrings_WhenCreatingCustomer_ThenEmptyStringsAreStored() {
        // Given
        String id = "";
        String name = "";
        String email = "";
        
        // When
        Customer customer = new Customer(id, name, email);
        
        // Then
        assertThat(customer.getId()).isEmpty();
        assertThat(customer.getName()).isEmpty();
        assertThat(customer.getEmail()).isEmpty();
    }

    @Test
    @DisplayName("Given long values, When creating Customer, Then long values are stored correctly")
    void givenLongValues_WhenCreatingCustomer_ThenLongValuesAreStoredCorrectly() {
        // Given
        String longId = "a".repeat(1000);
        String longName = "John " + "Doe".repeat(100);
        String longEmail = "very.long.email." + "address".repeat(50) + "@example.com";
        
        // When
        Customer customer = new Customer(longId, longName, longEmail);
        
        // Then
        assertThat(customer.getId()).hasSize(1000);
        assertThat(customer.getName()).startsWith("John Doe");
        assertThat(customer.getEmail()).endsWith("@example.com");
        assertThat(customer.getId()).isEqualTo(longId);
        assertThat(customer.getName()).isEqualTo(longName);
        assertThat(customer.getEmail()).isEqualTo(longEmail);
    }

    @Test
    @DisplayName("Given special characters, When creating Customer, Then special characters are preserved")
    void givenSpecialCharacters_WhenCreatingCustomer_ThenSpecialCharactersArePreserved() {
        // Given
        String id = "customer-àáâãäåæçèéêë";
        String name = "José María Señor-Åström";
        String email = "josé+maría@señor-åström.com";
        
        // When
        Customer customer = new Customer(id, name, email);
        
        // Then
        assertThat(customer.getId()).isEqualTo(id);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getEmail()).isEqualTo(email);
    }
}