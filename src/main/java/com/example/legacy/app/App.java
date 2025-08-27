package com.example.legacy.app;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;
import com.example.legacy.service.OrderService;
import com.example.legacy.legacy.XmlMarshaller;
import com.example.legacy.legacy.LegacyBase64;
import com.example.legacy.legacy.LegacySoapEndpoint;
import com.example.legacy.security.SecurityBootstrap;

import java.time.Instant;
import java.util.UUID;

public class App {
    public static void main(String[] args) throws Exception {
        SecurityBootstrap.install();
        OrderRepository repository = new OrderRepository();
        OrderService service = new OrderService(repository);

    Customer c = new Customer(UUID.randomUUID().toString(), "Renzo Iwamoto", "renzo@example.com");
    Order o = service.createOrder(c, 149.90d, Instant.now());

        String xml = XmlMarshaller.toXml(o);
        System.out.println("XML generado (JAXB - JDK6):\n" + xml);

        String b64 = LegacyBase64.encode(xml.getBytes("UTF-8"));
        System.out.println("Base64 (sun.misc): " + b64);
        String xmlBack = new String(LegacyBase64.decode(b64), "UTF-8");
        System.out.println("Roundtrip Base64 OK: " + xmlBack.startsWith("<?xml"));

        LegacySoapEndpoint.publish(service);
        System.out.println("SOAP publicado en http://0.0.0.0:8080/legacy");

        System.out.println("Presiona Ctrl+C para finalizar");
        while (true) { Thread.sleep(30000L); }
    }
}
