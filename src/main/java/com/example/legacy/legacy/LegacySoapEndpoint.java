package com.example.legacy.legacy;

import com.example.legacy.service.OrderService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class LegacySoapEndpoint {
    public static void publish(OrderService service) throws IOException {
        LegacySoapService impl = new LegacySoapService(service);
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        server.createContext("/legacy", new SoapLikeHandler(impl));
        server.setExecutor(null);
        server.start();
    }

    static class SoapLikeHandler implements HttpHandler {
        private final LegacySoapService svc;
        SoapLikeHandler(LegacySoapService svc) { this.svc = svc; }

        @Override public void handle(HttpExchange ex) throws IOException {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            String body = readAll(ex.getRequestBody());
            String responseBody;
            if (body.contains("<leg:sayHello>")) {
                String name = extractArg0(body);
                String greet = svc.sayHello(name != null ? name : "mundo");
                responseBody = soapEnvelope("<sayHelloResponse><return>" + escape(greet) + "</return></sayHelloResponse>");
            } else {
                responseBody = soapEnvelope("<error>Unknown operation</error>");
            }
            byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().add("Content-Type", "text/xml; charset=utf-8");
            ex.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
        }

        private static String readAll(InputStream in) throws IOException {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }

        private static String extractArg0(String xml) {
            int s = xml.indexOf("<arg0>");
            int e = xml.indexOf("</arg0>");
            if (s >= 0 && e > s) return xml.substring(s + 6, e);
            return null;
        }

        private static String soapEnvelope(String inner) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<soapenv:Body>" + inner + "</soapenv:Body></soapenv:Envelope>";
        }

        private static String escape(String s) {
            return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }
    }
}
