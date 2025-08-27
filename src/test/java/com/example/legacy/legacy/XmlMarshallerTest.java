package com.example.legacy.legacy;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("XmlMarshaller Tests")
class XmlMarshallerTest {

    @Test
    @DisplayName("Given valid Order, When marshalling to XML, Then valid XML is generated")
    void givenValidOrder_WhenMarshallingToXml_ThenValidXmlIsGenerated() throws Exception {
        // Given
        Instant createdAt = Instant.parse("2024-01-15T10:30:00Z");
        Order order = new Order("order-123", "customer-456", 199.99, createdAt);
        
        // When
        String xml = XmlMarshaller.toXml(order);
        
        // Then
        assertThat(xml).isNotEmpty();
        assertThat(xml).contains("<?xml version=\"1.0\"");
        assertThat(xml).contains("<order>");
        assertThat(xml).contains("</order>");
        assertThat(xml).contains("order-123");
        assertThat(xml).contains("customer-456");
        assertThat(xml).contains("199.99");
    }

    @Test
    @DisplayName("Given Order with null values, When marshalling to XML, Then XML handles nulls appropriately")
    void givenOrderWithNullValues_WhenMarshallingToXml_ThenXmlHandlesNullsAppropriately() throws Exception {
        // Given
        Order order = new Order(null, null, 0.0, null);
        
        // When
        String xml = XmlMarshaller.toXml(order);
        
        // Then
        assertThat(xml).isNotEmpty();
        assertThat(xml).contains("<?xml version=\"1.0\"");
        assertThat(xml).contains("<order>");
        assertThat(xml).contains("</order>");
        assertThat(xml).contains("0.0");
    }

    @Test
    @DisplayName("Given Order with zero amount, When marshalling to XML, Then zero amount is included")
    void givenOrderWithZeroAmount_WhenMarshallingToXml_ThenZeroAmountIsIncluded() throws Exception {
        // Given
        Instant createdAt = Instant.parse("2024-01-15T10:30:00Z");
        Order order = new Order("order-0", "customer-0", 0.0, createdAt);
        
        // When
        String xml = XmlMarshaller.toXml(order);
        
        // Then
        assertThat(xml).contains("0.0");
        assertThat(xml).contains("order-0");
        assertThat(xml).contains("customer-0");
    }

    @Test
    @DisplayName("Given Order with negative amount, When marshalling to XML, Then negative amount is included")
    void givenOrderWithNegativeAmount_WhenMarshallingToXml_ThenNegativeAmountIsIncluded() throws Exception {
        // Given
        Instant createdAt = Instant.parse("2024-01-15T10:30:00Z");
        Order order = new Order("refund-1", "customer-1", -50.0, createdAt);
        
        // When
        String xml = XmlMarshaller.toXml(order);
        
        // Then
        assertThat(xml).contains("-50.0");
        assertThat(xml).contains("refund-1");
    }

    @Test
    @DisplayName("Given valid XML, When unmarshalling from XML, Then Order object is created correctly")
    void givenValidXml_WhenUnmarshallingFromXml_ThenOrderObjectIsCreatedCorrectly() throws Exception {
        // Given
        String xml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <order>
                <amount>299.95</amount>
                <createdAt>2024-01-15T10:30:00Z</createdAt>
                <customerId>customer-789</customerId>
                <id>order-789</id>
            </order>
            """;
        
        // When
        Order order = XmlMarshaller.fromXml(xml);
        
        // Then
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo("order-789");
        assertThat(order.getCustomerId()).isEqualTo("customer-789");
        assertThat(order.getAmount()).isEqualTo(299.95);
        assertThat(order.getCreatedAt()).isEqualTo(Instant.parse("2024-01-15T10:30:00Z"));
    }

    @Test
    @DisplayName("Given round-trip marshalling and unmarshalling, When processing Order, Then Order is preserved")
    void givenRoundTripMarshallingAndUnmarshalling_WhenProcessingOrder_ThenOrderIsPreserved() throws Exception {
        // Given
        Instant originalTime = Instant.parse("2024-02-01T14:15:30Z");
        Order originalOrder = new Order("round-trip-1", "customer-rt", 123.45, originalTime);
        
        // When
        String xml = XmlMarshaller.toXml(originalOrder);
        Order reconstructedOrder = XmlMarshaller.fromXml(xml);
        
        // Then
        assertThat(reconstructedOrder).isNotNull();
        assertThat(reconstructedOrder.getId()).isEqualTo(originalOrder.getId());
        assertThat(reconstructedOrder.getCustomerId()).isEqualTo(originalOrder.getCustomerId());
        assertThat(reconstructedOrder.getAmount()).isEqualTo(originalOrder.getAmount());
        assertThat(reconstructedOrder.getCreatedAt()).isEqualTo(originalOrder.getCreatedAt());
    }

    @Test
    @DisplayName("Given null Order, When marshalling to XML, Then exception is thrown")
    void givenNullOrder_WhenMarshallingToXml_ThenExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> XmlMarshaller.toXml(null))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Given null XML string, When unmarshalling from XML, Then exception is thrown")
    void givenNullXmlString_WhenUnmarshallingFromXml_ThenExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> XmlMarshaller.fromXml(null))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Given empty XML string, When unmarshalling from XML, Then exception is thrown")
    void givenEmptyXmlString_WhenUnmarshallingFromXml_ThenExceptionIsThrown() {
        // When & Then
        assertThatThrownBy(() -> XmlMarshaller.fromXml(""))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Given invalid XML string, When unmarshalling from XML, Then exception is thrown")
    void givenInvalidXmlString_WhenUnmarshallingFromXml_ThenExceptionIsThrown() {
        // Given
        String invalidXml = "This is not valid XML at all!";
        
        // When & Then
        assertThatThrownBy(() -> XmlMarshaller.fromXml(invalidXml))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Given XML with wrong root element, When unmarshalling from XML, Then exception is thrown")
    void givenXmlWithWrongRootElement_WhenUnmarshallingFromXml_ThenExceptionIsThrown() {
        // Given
        String wrongRootXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <wrongroot>
                <amount>100.0</amount>
            </wrongroot>
            """;
        
        // When & Then
        assertThatThrownBy(() -> XmlMarshaller.fromXml(wrongRootXml))
            .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Given Order with special characters, When marshalling and unmarshalling, Then special characters are preserved")
    void givenOrderWithSpecialCharacters_WhenMarshallingAndUnmarshalling_ThenSpecialCharactersArePreserved() throws Exception {
        // Given
        String specialId = "order-àáâãäåæçèéêë";
        String specialCustomerId = "customer-ñandú";
        Instant createdAt = Instant.parse("2024-01-15T10:30:00Z");
        Order originalOrder = new Order(specialId, specialCustomerId, 99.99, createdAt);
        
        // When
        String xml = XmlMarshaller.toXml(originalOrder);
        Order reconstructedOrder = XmlMarshaller.fromXml(xml);
        
        // Then
        assertThat(reconstructedOrder.getId()).isEqualTo(specialId);
        assertThat(reconstructedOrder.getCustomerId()).isEqualTo(specialCustomerId);
        assertThat(xml).contains(specialId);
        assertThat(xml).contains(specialCustomerId);
    }
}