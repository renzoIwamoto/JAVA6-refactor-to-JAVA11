package com.example.legacy.legacy;

import com.example.legacy.model.Order;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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

    public static Order fromXml(String xml) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(Order.class);
        Unmarshaller u = ctx.createUnmarshaller();
        return (Order) u.unmarshal(new StringReader(xml));
    }
}
