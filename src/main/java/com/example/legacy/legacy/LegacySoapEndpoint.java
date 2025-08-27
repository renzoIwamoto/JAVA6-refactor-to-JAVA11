package com.example.legacy.legacy;

import javax.xml.ws.Endpoint;

public class LegacySoapEndpoint {
    
    private static final String ENDPOINT_URL = "http://0.0.0.0:8080/legacy";
    private Endpoint endpoint;
    
    public void startSoapService() {
        try {
            System.out.println("=== STARTING LEGACY SOAP SERVICE ===");
            System.out.println("Using javax.xml.ws.Endpoint (built into Java 6 JDK)");
            System.out.println("WARNING: javax.xml.ws was REMOVED in Java 11+");
            System.out.println("Endpoint URL: " + ENDPOINT_URL);
            
            LegacySoapService service = new LegacySoapService();
            endpoint = Endpoint.publish(ENDPOINT_URL, service);
            
            if (endpoint.isPublished()) {
                System.out.println("✓ SOAP service successfully published at " + ENDPOINT_URL);
                System.out.println("✓ WSDL available at: " + ENDPOINT_URL + "?wsdl");
                System.out.println("==================================");
                
                printServiceOperations();
            } else {
                System.err.println("✗ Failed to publish SOAP service");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error starting SOAP service: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to start SOAP service", e);
        }
    }
    
    public void stopSoapService() {
        if (endpoint != null && endpoint.isPublished()) {
            try {
                endpoint.stop();
                System.out.println("✓ SOAP service stopped");
            } catch (Exception e) {
                System.err.println("✗ Error stopping SOAP service: " + e.getMessage());
            }
        }
    }
    
    public boolean isServiceRunning() {
        return endpoint != null && endpoint.isPublished();
    }
    
    public String getEndpointUrl() {
        return ENDPOINT_URL;
    }
    
    public String getWsdlUrl() {
        return ENDPOINT_URL + "?wsdl";
    }
    
    private void printServiceOperations() {
        System.out.println("\n=== AVAILABLE SOAP OPERATIONS ===");
        System.out.println("• createOrder(customerId, customerName, customerEmail, productName, quantity, unitPrice)");
        System.out.println("• getOrderAsXml(orderId) - Returns XML representation");
        System.out.println("• processOrder(orderId) - Changes status to PROCESSING");
        System.out.println("• completeOrder(orderId) - Changes status to COMPLETED");
        System.out.println("• getAllOrdersCount() - Returns total order count");
        System.out.println("• getOrderReport(orderId) - Returns formatted report");
        System.out.println("• getTotalRevenue() - Returns sum of completed orders");
        System.out.println("• getServerInfo() - Returns service information");
        System.out.println("================================\n");
        
        System.out.println("Test with curl:");
        System.out.println("curl -X POST -H \"Content-Type: text/xml\" -H \"SOAPAction: \\\"\\\"\" \\");
        System.out.println("  -d @soap_request.xml " + ENDPOINT_URL);
        System.out.println("");
    }
    
    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("\nShutting down SOAP service...");
                stopSoapService();
            }
        });
    }
}