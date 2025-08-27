package com.example.legacy.app;

import com.example.legacy.legacy.LegacyBase64;
import com.example.legacy.legacy.LegacySoapEndpoint;
import com.example.legacy.legacy.XmlMarshaller;
import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.security.SecurityBootstrap;
import com.example.legacy.service.OrderService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class App {
    
    private static final String SEPARATOR = "============================================================";
    
    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("LEGACY ORDERS JAVA 6 APPLICATION");
        System.out.println("Demonstrating Java 6 features that BREAK in modern JDK");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println(SEPARATOR);
        
        try {
            SecurityBootstrap.installSecurityManager();
            SecurityBootstrap.checkSecurityManagerStatus();
            
            demonstrateOrderManagement();
            demonstrateJaxbXmlSerialization();
            demonstrateLegacyBase64Encoding();
            startSoapService();
            
            System.out.println("\n" + SEPARATOR);
            System.out.println("APPLICATION STARTED SUCCESSFULLY!");
            System.out.println("Press Ctrl+C to stop the application");
            System.out.println(SEPARATOR);
            
            while (true) {
                try {
                    Thread.sleep(5000);
                    System.out.println("Application running... " + new Date());
                } catch (InterruptedException e) {
                    break;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void demonstrateOrderManagement() {
        System.out.println("\n=== DEMONSTRATING ORDER MANAGEMENT ===");
        
        OrderService orderService = new OrderService();
        
        Customer customer1 = new Customer(1L, "John Doe", "john@example.com");
        Customer customer2 = new Customer(2L, "Jane Smith", "jane@example.com");
        
        Order order1 = orderService.createOrder(customer1, "Laptop", 2, new BigDecimal("999.99"));
        Order order2 = orderService.createOrder(customer2, "Mouse", 5, new BigDecimal("25.50"));
        Order order3 = orderService.createOrder(customer1, "Keyboard", 1, new BigDecimal("75.00"));
        
        System.out.println("✓ Created " + orderService.findAllOrders().size() + " orders");
        
        orderService.processOrder(order1.getId());
        orderService.completeOrder(order1.getId());
        
        System.out.println("✓ Processed and completed order: " + order1.getId());
        System.out.println("✓ Total revenue: $" + orderService.calculateTotalRevenue());
        
        List<Order> recentOrders = orderService.findOrdersFromLastDays(1);
        System.out.println("✓ Orders from last day: " + recentOrders.size());
        
        System.out.println("NOTE: Uses java.util.Date/Calendar (legacy date handling)");
        System.out.println("NOTE: Uses Collections.synchronizedList (thread-safe collections)");
    }
    
    private static void demonstrateJaxbXmlSerialization() {
        System.out.println("\n=== DEMONSTRATING JAXB XML SERIALIZATION ===");
        
        try {
            Customer customer = new Customer(100L, "Test Customer", "test@example.com");
            Order order = new Order(1000L, customer, "Test Product", 3, new BigDecimal("49.99"));
            
            XmlMarshaller marshaller = new XmlMarshaller();
            String xml = marshaller.getFormattedXml(order);
            
            System.out.println("✓ JAXB marshalling successful");
            System.out.println("✓ Uses javax.xml.bind (included in Java 6 JDK)");
            System.out.println("✗ BREAKS in Java 11+: javax.xml.bind removed from JDK");
            
            Order unmarshalled = marshaller.unmarshalOrderFromXml(
                marshaller.marshalOrderToXml(order));
            System.out.println("✓ JAXB unmarshalling successful");
            System.out.println("✓ Order ID after round-trip: " + unmarshalled.getId());
            
        } catch (Exception e) {
            System.err.println("✗ JAXB demonstration failed: " + e.getMessage());
        }
    }
    
    private static void demonstrateLegacyBase64Encoding() {
        System.out.println("\n=== DEMONSTRATING LEGACY BASE64 ENCODING ===");
        
        try {
            LegacyBase64 base64 = new LegacyBase64();
            
            String testData = "Order #12345: Customer John Doe purchased 2 laptops for $1999.98";
            String encoded = base64.encode(testData);
            String decoded = base64.decode(encoded);
            
            System.out.println("✓ Base64 encoding successful");
            System.out.println("✓ Uses sun.misc.BASE64Encoder/BASE64Decoder");
            System.out.println("✗ BREAKS in Java 9+: sun.misc classes encapsulated/removed");
            System.out.println("✓ Original data matches after encoding/decoding: " + testData.equals(decoded));
            
            String orderSummary = base64.encodeOrderSummary("12345", "John Doe", "$1999.98");
            System.out.println("✓ Encoded order summary length: " + orderSummary.length() + " characters");
            
        } catch (Exception e) {
            System.err.println("✗ Base64 demonstration failed: " + e.getMessage());
        }
    }
    
    private static void startSoapService() {
        System.out.println("\n=== STARTING SOAP WEB SERVICE ===");
        
        try {
            LegacySoapEndpoint endpoint = new LegacySoapEndpoint();
            endpoint.addShutdownHook();
            endpoint.startSoapService();
            
            System.out.println("✓ Uses javax.xml.ws.Endpoint (included in Java 6 JDK)");
            System.out.println("✗ BREAKS in Java 11+: javax.xml.ws removed from JDK");
            System.out.println("✓ Service URL: " + endpoint.getEndpointUrl());
            System.out.println("✓ WSDL URL: " + endpoint.getWsdlUrl());
            
        } catch (Exception e) {
            System.err.println("✗ SOAP service startup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void printSystemInformation() {
        System.out.println("\n=== SYSTEM INFORMATION ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Available Processors: " + runtime.availableProcessors());
        System.out.println("Max Memory: " + (runtime.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("Total Memory: " + (runtime.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("Free Memory: " + (runtime.freeMemory() / 1024 / 1024) + " MB");
        
        SecurityManager sm = System.getSecurityManager();
        System.out.println("Security Manager: " + (sm != null ? sm.getClass().getSimpleName() : "None"));
    }
}