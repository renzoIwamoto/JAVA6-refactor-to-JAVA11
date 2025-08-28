package com.example.legacy.legacy;

import com.example.legacy.model.Order;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;

public class XmlMarshaller {
    public static String toXml(Order o) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("order");
        doc.appendChild(root);

        appendText(doc, root, "id", o.getId());
        appendText(doc, root, "customerId", o.getCustomerId());
        appendText(doc, root, "amount", Double.toString(o.getAmount()));
        appendText(doc, root, "createdAt", o.getCreatedAt().toString());

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
    }

    public static Order fromXml(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xml)));
        Element root = (Element) doc.getElementsByTagName("order").item(0);
        String id = text(root, "id");
        String customerId = text(root, "customerId");
        double amount = Double.parseDouble(text(root, "amount"));
        Instant createdAt = Instant.parse(text(root, "createdAt"));
        return new Order(id, customerId, amount, createdAt);
    }

    private static void appendText(Document doc, Element parent, String name, String value) {
        Element e = doc.createElement(name);
        e.appendChild(doc.createTextNode(value));
        parent.appendChild(e);
    }

    private static String text(Element parent, String name) {
        return parent.getElementsByTagName(name).item(0).getTextContent();
    }
}
