package com.example.legacy.legacy;

import com.example.legacy.model.Order;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlMarshaller {
    public static String toXml(Order order) throws Exception {
        var context = JAXBContext.newInstance(Order.class);
        var marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        var writer = new StringWriter();
        marshaller.marshal(order, writer);
        return writer.toString();
    }

    public static Order fromXml(String xml) throws Exception {
        var context = JAXBContext.newInstance(Order.class);
        var unmarshaller = context.createUnmarshaller();
        return (Order) unmarshaller.unmarshal(new StringReader(xml));
    }
}
