package com.example.legacy.legacy;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class XmlMarshallerTest {

    @Test
    void givenValidOrder_whenMarshallingToXml_thenReturnsValidXmlString() throws Exception {
        // Given
        var order = new Order("order-123", "customer-456", 99.99, Instant.parse("2023-01-01T10:00:00Z"));

        // When
        var xml = XmlMarshaller.toXml(order);

        // Then
        assertNotNull(xml);
        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("<order>"));
        assertTrue(xml.contains("order-123"));
        assertTrue(xml.contains("customer-456"));
        assertTrue(xml.contains("99.99"));
        assertTrue(xml.contains("2023-01-01T10:00:00Z"));
    }

    @Test
    void givenOrderWithNullValues_whenMarshallingToXml_thenReturnsXmlWithEmptyElements() throws Exception {
        // Given
        var order = new Order();

        // When
        var xml = XmlMarshaller.toXml(order);

        // Then
        assertNotNull(xml);
        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("<order>"));
        assertTrue(xml.contains("</order>"));
    }

    @Test
    void givenOrderWithZeroAmount_whenMarshallingToXml_thenReturnsXmlWithZeroAmount() throws Exception {
        // Given
        var order = new Order("id", "customer", 0.0, Instant.now());

        // When
        var xml = XmlMarshaller.toXml(order);

        // Then
        assertNotNull(xml);
        assertTrue(xml.contains("0.0"));
    }

    @Test
    void givenOrderWithNegativeAmount_whenMarshallingToXml_thenReturnsXmlWithNegativeAmount() throws Exception {
        // Given
        var order = new Order("id", "customer", -50.0, Instant.now());

        // When
        var xml = XmlMarshaller.toXml(order);

        // Then
        assertNotNull(xml);
        assertTrue(xml.contains("-50.0"));
    }

    @Test
    void givenValidXmlString_whenUnmarshallingFromXml_thenReturnsValidOrder() throws Exception {
        // Given
        var xml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <order>
                <id>order-123</id>
                <customerId>customer-456</customerId>
                <amount>99.99</amount>
                <createdAt>2023-01-01T10:00:00Z</createdAt>
            </order>
            """;

        // When
        var order = XmlMarshaller.fromXml(xml);

        // Then
        assertNotNull(order);
        assertEquals("order-123", order.getId());
        assertEquals("customer-456", order.getCustomerId());
        assertEquals(99.99, order.getAmount());
        assertEquals(Instant.parse("2023-01-01T10:00:00Z"), order.getCreatedAt());
    }

    @Test
    void givenXmlWithEmptyElements_whenUnmarshallingFromXml_thenReturnsOrderWithNullValues() throws Exception {
        // Given
        var xml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <order>
                <amount>0.0</amount>
            </order>
            """;

        // When
        var order = XmlMarshaller.fromXml(xml);

        // Then
        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getCustomerId());
        assertEquals(0.0, order.getAmount());
        assertNull(order.getCreatedAt());
    }

    @Test
    void givenOrder_whenMarshallingAndUnmarshalling_thenReturnsEquivalentOrder() throws Exception {
        // Given
        var originalOrder = new Order("order-789", "customer-101", 150.75, Instant.parse("2023-06-15T14:30:00Z"));

        // When
        var xml = XmlMarshaller.toXml(originalOrder);
        var unmarshalledOrder = XmlMarshaller.fromXml(xml);

        // Then
        assertNotNull(unmarshalledOrder);
        assertEquals(originalOrder.getId(), unmarshalledOrder.getId());
        assertEquals(originalOrder.getCustomerId(), unmarshalledOrder.getCustomerId());
        assertEquals(originalOrder.getAmount(), unmarshalledOrder.getAmount());
        assertEquals(originalOrder.getCreatedAt(), unmarshalledOrder.getCreatedAt());
    }

    @Test
    void givenInvalidXmlString_whenUnmarshallingFromXml_thenThrowsException() {
        // Given
        var invalidXml = "This is not valid XML";

        // When & Then
        assertThrows(Exception.class, () -> {
            XmlMarshaller.fromXml(invalidXml);
        });
    }

    @Test
    void givenNullOrder_whenMarshallingToXml_thenThrowsException() {
        // Given & When & Then
        assertThrows(Exception.class, () -> {
            XmlMarshaller.toXml(null);
        });
    }

    @Test
    void givenNullXmlString_whenUnmarshallingFromXml_thenThrowsException() {
        // Given & When & Then
        assertThrows(Exception.class, () -> {
            XmlMarshaller.fromXml(null);
        });
    }
}