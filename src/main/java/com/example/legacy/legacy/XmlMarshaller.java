package com.example.legacy.legacy;

import com.example.legacy.model.Order;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

public class XmlMarshaller {
    public static String toXml(Order order) throws Exception {
        Objects.requireNonNull(order, "Order cannot be null");
        
        var jaxbContext = JAXBContext.newInstance(Order.class);
        var xmlMarshaller = jaxbContext.createMarshaller();
        xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        var xmlStringWriter = new StringWriter();
        xmlMarshaller.marshal(order, xmlStringWriter);
        return xmlStringWriter.toString();
    }

    public static Order fromXml(String xmlContent) throws Exception {
        Objects.requireNonNull(xmlContent, "XML content cannot be null");
        if (xmlContent.isBlank()) {
            throw new IllegalArgumentException("XML content cannot be blank");
        }
        
        var jaxbContext = JAXBContext.newInstance(Order.class);
        var xmlUnmarshaller = jaxbContext.createUnmarshaller();
        
        var unmarshalledObject = xmlUnmarshaller.unmarshal(new StringReader(xmlContent));
        if (unmarshalledObject instanceof Order deserializedOrder) {
            return deserializedOrder;
        }
        
        throw new IllegalArgumentException("XML does not represent a valid Order object");
    }
}
