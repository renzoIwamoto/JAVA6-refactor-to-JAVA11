package com.example.legacy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(customer.id()).isEqualTo(id);
        assertThat(customer.name()).isEqualTo(name);
        assertThat(customer.email()).isEqualTo(email);
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
            assertThat(customer.id()).isEqualTo(id);
            assertThat(customer.name()).isEqualTo(name);
            assertThat(customer.email()).isEqualTo(email);
        }
    }

    @Test
    @DisplayName("Given null values, When creating Customer, Then exception is thrown")
    void givenNullValues_WhenCreatingCustomer_ThenExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> new Customer(null, "name", "email"))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Customer ID cannot be null");
            
        assertThatThrownBy(() -> new Customer("id", null, "email"))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Customer name cannot be null");
            
        assertThatThrownBy(() -> new Customer("id", "name", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Customer email cannot be null");
    }

    @Test
    @DisplayName("Given empty strings, When creating Customer, Then exception is thrown")
    void givenEmptyStrings_WhenCreatingCustomer_ThenExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> new Customer("", "name", "email"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer ID cannot be blank");
            
        assertThatThrownBy(() -> new Customer("id", "", "email"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer name cannot be blank");
            
        assertThatThrownBy(() -> new Customer("id", "name", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer email cannot be blank");
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
        assertThat(customer.id()).hasSize(1000);
        assertThat(customer.name()).startsWith("John Doe");
        assertThat(customer.email()).endsWith("@example.com");
        assertThat(customer.id()).isEqualTo(longId);
        assertThat(customer.name()).isEqualTo(longName);
        assertThat(customer.email()).isEqualTo(longEmail);
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
        assertThat(customer.id()).isEqualTo(id);
        assertThat(customer.name()).isEqualTo(name);
        assertThat(customer.email()).isEqualTo(email);
    }
}