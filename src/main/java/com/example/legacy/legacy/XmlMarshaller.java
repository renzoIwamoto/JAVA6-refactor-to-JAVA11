package com.example.legacy.legacy;

import com.example.legacy.model.Order;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlMarshaller {
    public static String toXml(Order o) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(Order.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        m.marshal(o, sw);
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    public static Order fromXml(String xml) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(Order.class);
        Unmarshaller u = ctx.createUnmarshaller();
        return (Order) u.unmarshal(new StringReader(xml));
    }
}
