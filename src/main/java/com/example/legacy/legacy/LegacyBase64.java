package com.example.legacy.legacy;

import java.util.Base64;

public class LegacyBase64 {
    public static String encode(byte[] binaryData) {
        return Base64.getEncoder().encodeToString(binaryData);
    }

    public static byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
}
