package com.example.legacy.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    private static final String SAMPLE_ID = "test-id-123";
    private static final String SAMPLE_CUSTOMER_ID = "customer-456";
    private static final double SAMPLE_AMOUNT = 149.90;
    private static final Instant SAMPLE_TIME = Instant.parse("2025-08-26T10:15:30.00Z");

    @Test
    void givenValidData_whenCreatingOrder_thenAllFieldsAreSet() {
        // Given
        Order order = new Order(SAMPLE_ID, SAMPLE_CUSTOMER_ID, SAMPLE_AMOUNT, SAMPLE_TIME);

        // When/Then
        assertThat(order.getId()).isEqualTo(SAMPLE_ID);
        assertThat(order.getCustomerId()).isEqualTo(SAMPLE_CUSTOMER_ID);
        assertThat(order.getAmount()).isEqualTo(SAMPLE_AMOUNT);
        assertThat(order.getCreatedAt()).isEqualTo(SAMPLE_TIME);
    }

    @Test
    void givenOrder_whenSettingNewValues_thenFieldsAreUpdated() {
        // Given
        Order order = new Order();
        
        // When
        order.setId(SAMPLE_ID);
        order.setCustomerId(SAMPLE_CUSTOMER_ID);
        order.setAmount(SAMPLE_AMOUNT);
        order.setCreatedAt(SAMPLE_TIME);

        // Then
        assertThat(order.getId()).isEqualTo(SAMPLE_ID);
        assertThat(order.getCustomerId()).isEqualTo(SAMPLE_CUSTOMER_ID);
        assertThat(order.getAmount()).isEqualTo(SAMPLE_AMOUNT);
        assertThat(order.getCreatedAt()).isEqualTo(SAMPLE_TIME);
    }
}
