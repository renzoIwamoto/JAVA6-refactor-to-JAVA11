package com.example.legacy.legacy;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class XmlMarshallerTest {

    @Test
    void givenOrder_whenToXml_thenContainsAllFields() throws Exception {
        Order o = new Order("ID-1", "CUST-9", 42.5, Instant.parse("2020-01-01T00:00:00Z"));
        String xml = XmlMarshaller.toXml(o);
        assertTrue(xml.contains("<order>"));
        assertTrue(xml.contains("ID-1"));
        assertTrue(xml.contains("CUST-9"));
        assertTrue(xml.contains("42.5"));
        assertTrue(xml.contains("2020-01-01T00:00:00Z"));
    }

    @Test
    void givenXml_whenFromXml_thenRoundtripMatches() throws Exception {
        Order original = new Order("ID-2", "C2", 10.0, Instant.parse("2022-02-02T02:02:02Z"));
        String xml = XmlMarshaller.toXml(original);
        Order parsed = XmlMarshaller.fromXml(xml);

        assertEquals(original.getId(), parsed.getId());
        assertEquals(original.getCustomerId(), parsed.getCustomerId());
        assertEquals(original.getAmount(), parsed.getAmount());
        assertEquals(original.getCreatedAt(), parsed.getCreatedAt());
    }
}

