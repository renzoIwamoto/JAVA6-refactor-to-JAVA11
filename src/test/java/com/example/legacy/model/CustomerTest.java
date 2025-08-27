package com.example.legacy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void givenValidCustomerData_whenCreatingCustomer_thenCustomerIsCreatedCorrectly() {
        // Given
        var id = "customer-123";
        var name = "John Doe";
        var email = "john.doe@example.com";

        // When
        var customer = new Customer(id, name, email);

        // Then
        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(email, customer.getEmail());
    }

    @Test
    void givenNullValues_whenCreatingCustomer_thenCustomerIsCreatedWithNullValues() {
        // Given & When
        var customer = new Customer(null, null, null);

        // Then
        assertNull(customer.getId());
        assertNull(customer.getName());
        assertNull(customer.getEmail());
    }

    @Test
    void givenEmptyStrings_whenCreatingCustomer_thenCustomerIsCreatedWithEmptyStrings() {
        // Given
        var id = "";
        var name = "";
        var email = "";

        // When
        var customer = new Customer(id, name, email);

        // Then
        assertEquals("", customer.getId());
        assertEquals("", customer.getName());
        assertEquals("", customer.getEmail());
    }
}