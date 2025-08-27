package com.example.legacy.legacy;

import com.example.legacy.model.Order;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlMarshaller {
    
    private final JAXBContext jaxbContext;
    
    public XmlMarshaller() {
        try {
            this.jaxbContext = JAXBContext.newInstance(Order.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to initialize JAXB context", e);
        }
    }
    
    public String marshalOrderToXml(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            
            StringWriter writer = new StringWriter();
            marshaller.marshal(order, writer);
            
            return writer.toString();
            
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to marshal order to XML: " + e.getMessage(), e);
        }
    }
    
    public Order unmarshalOrderFromXml(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            throw new IllegalArgumentException("XML string cannot be null or empty");
        }
        
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            
            Object result = unmarshaller.unmarshal(reader);
            if (result instanceof Order) {
                return (Order) result;
            } else {
                throw new RuntimeException("Unmarshalled object is not an Order instance");
            }
            
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to unmarshal XML to order: " + e.getMessage(), e);
        }
    }
    
    public boolean isValidOrderXml(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            return false;
        }
        
        try {
            unmarshalOrderFromXml(xml);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getFormattedXml(Order order) {
        String xml = marshalOrderToXml(order);
        
        StringBuilder formatted = new StringBuilder();
        formatted.append("<!-- LEGACY JAXB XML SERIALIZATION -->\n");
        formatted.append("<!-- This uses javax.xml.bind which is included in Java 6 JDK -->\n");
        formatted.append("<!-- In Java 11+, javax.xml.bind was REMOVED from JDK -->\n");
        formatted.append("<!-- Migration: Add jakarta.xml.bind-api + implementation -->\n");
        formatted.append("<!-- and change imports from javax.* to jakarta.* -->\n");
        formatted.append(xml);
        
        return formatted.toString();
    }
}