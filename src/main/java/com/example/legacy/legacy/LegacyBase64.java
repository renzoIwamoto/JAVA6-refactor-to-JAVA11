package com.example.legacy.legacy;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class LegacyBase64 {
    public static String encode(byte[] data) throws Exception {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(data);
    }

    public static byte[] decode(String base64) throws Exception {
        BASE64Decoder dec = new BASE64Decoder();
        return dec.decodeBuffer(base64);
    }
}
