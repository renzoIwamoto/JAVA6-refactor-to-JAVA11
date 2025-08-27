package com.example.legacy.legacy;

import com.example.legacy.service.OrderService;
import javax.xml.ws.Endpoint;

public class LegacySoapEndpoint {
    public static void publish(OrderService service) {
        LegacySoapService impl = new LegacySoapService(service);
        Endpoint.publish("http://0.0.0.0:8080/legacy", impl);
    }
}
