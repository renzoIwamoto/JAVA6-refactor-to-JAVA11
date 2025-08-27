package com.example.legacy.legacy;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XmlMarshallerTest {
    private static final Instant FIXED_TIME = Instant.parse("2025-08-26T10:15:30.00Z");
    private Order sampleOrder;
    private static final String EXPECTED_XML = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <order>
                <id>test-123</id>
                <customerId>cust-456</customerId>
                <amount>99.99</amount>
                <createdAt>2025-08-26T10:15:30.00Z</createdAt>
            </order>
            """.trim();

    @BeforeEach
    void setUp() {
        sampleOrder = new Order("test-123", "cust-456", 99.99, FIXED_TIME);
    }

    @Test
    void givenOrder_whenMarshallingToXml_thenGeneratesValidXml() throws Exception {
        // When
        String result = XmlMarshaller.toXml(sampleOrder);

        // Then
        assertThat(result.trim()).isEqualToNormalizingNewlines(EXPECTED_XML);
    }

    @Test
    void givenValidXml_whenUnmarshalling_thenReturnsOrder() throws Exception {
        // When
        Order result = XmlMarshaller.fromXml(EXPECTED_XML);

        // Then
        assertThat(result.getId()).isEqualTo(sampleOrder.getId());
        assertThat(result.getCustomerId()).isEqualTo(sampleOrder.getCustomerId());
        assertThat(result.getAmount()).isEqualTo(sampleOrder.getAmount());
        assertThat(result.getCreatedAt()).isEqualTo(sampleOrder.getCreatedAt());
    }

    @Test
    void givenInvalidXml_whenUnmarshalling_thenThrowsException() {
        // Given
        String invalidXml = "<invalid>not an order</invalid>";

        // When/Then
        assertThrows(Exception.class, () -> XmlMarshaller.fromXml(invalidXml));
    }

    @Test
    void givenSpecialCharacters_whenMarshallingAndUnmarshalling_thenPreservesData() throws Exception {
        // Given
        Order orderWithSpecialChars = new Order(
            "test & 123",
            "customer < 456 >",
            99.99,
            FIXED_TIME
        );

        // When
        String xml = XmlMarshaller.toXml(orderWithSpecialChars);
        Order result = XmlMarshaller.fromXml(xml);

        // Then
        assertThat(result.getId()).isEqualTo(orderWithSpecialChars.getId());
        assertThat(result.getCustomerId()).isEqualTo(orderWithSpecialChars.getCustomerId());
    }
}
