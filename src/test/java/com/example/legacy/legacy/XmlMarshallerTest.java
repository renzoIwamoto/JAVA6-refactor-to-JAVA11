package com.example.legacy.legacy;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlMarshallerTest {

    @Test
    void givenOrder_whenToXmlAndFromXml_thenObjectsAreEqual() throws Exception {
        // Given
        Instant now = Instant.parse("2023-10-27T10:00:00Z");
        Order order = new Order(UUID.randomUUID().toString(), "customer-1", 100.0, now);

        // When
        String xml = XmlMarshaller.toXml(order);
        Order unmarshalledOrder = XmlMarshaller.fromXml(xml);

        // Then
        assertEquals(order.getId(), unmarshalledOrder.getId());
        assertEquals(order.getCustomerId(), unmarshalledOrder.getCustomerId());
        assertEquals(order.getAmount(), unmarshalledOrder.getAmount());
        assertEquals(order.getCreatedAt(), unmarshalledOrder.getCreatedAt());
    }
}
