package com.example.legacy.security;

public class SecurityBootstrap {
    public static void install() {
        // SecurityManager was deprecated for removal and is not supported on Java 21.
        System.out.println("SecurityManager no soportado en Java 21; omitiendo instalación.");
    }
}
