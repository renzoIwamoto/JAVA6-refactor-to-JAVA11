package com.example.legacy.legacy;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class LegacyBase64 {
    
    private final BASE64Encoder encoder;
    private final BASE64Decoder decoder;
    
    public LegacyBase64() {
        this.encoder = new BASE64Encoder();
        this.decoder = new BASE64Decoder();
    }
    
    public String encode(String plainText) {
        if (plainText == null) {
            throw new IllegalArgumentException("Input text cannot be null");
        }
        
        try {
            byte[] bytes = plainText.getBytes("UTF-8");
            return encoder.encode(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode string to Base64: " + e.getMessage(), e);
        }
    }
    
    public String encode(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        
        return encoder.encode(data);
    }
    
    public String decode(String base64Text) {
        if (base64Text == null) {
            throw new IllegalArgumentException("Base64 text cannot be null");
        }
        
        try {
            byte[] decoded = decoder.decodeBuffer(base64Text);
            return new String(decoded, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to decode Base64 string: " + e.getMessage(), e);
        }
    }
    
    public byte[] decodeToBytes(String base64Text) {
        if (base64Text == null) {
            throw new IllegalArgumentException("Base64 text cannot be null");
        }
        
        try {
            return decoder.decodeBuffer(base64Text);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decode Base64 to bytes: " + e.getMessage(), e);
        }
    }
    
    public String encodeXmlContent(String xmlContent) {
        if (xmlContent == null) {
            throw new IllegalArgumentException("XML content cannot be null");
        }
        
        String encoded = encode(xmlContent);
        
        StringBuilder result = new StringBuilder();
        result.append("<!-- LEGACY BASE64 ENCODING -->\n");
        result.append("<!-- Uses sun.misc.BASE64Encoder/BASE64Decoder -->\n");
        result.append("<!-- These classes were REMOVED/ENCAPSULATED in JDK 9+ -->\n");
        result.append("<!-- Migration: Use java.util.Base64 instead -->\n");
        result.append("<!-- java.util.Base64.getEncoder().encodeToString(bytes) -->\n");
        result.append("<base64Content>\n");
        result.append(encoded);
        result.append("\n</base64Content>");
        
        return result.toString();
    }
    
    public boolean isValidBase64(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        try {
            decode(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String encodeOrderSummary(String orderId, String customerName, String total) {
        StringBuilder summary = new StringBuilder();
        summary.append("ORDER_ID:").append(orderId).append("\n");
        summary.append("CUSTOMER:").append(customerName).append("\n");
        summary.append("TOTAL:").append(total).append("\n");
        summary.append("ENCODED_WITH:sun.misc.BASE64Encoder\n");
        
        return encode(summary.toString());
    }
}