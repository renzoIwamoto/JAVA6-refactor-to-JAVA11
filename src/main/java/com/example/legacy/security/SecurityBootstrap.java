package com.example.legacy.security;

public class SecurityBootstrap {
    public static void install() {
        System.out.println("ADVERTENCIA: SecurityManager está obsoleto para eliminación en Java 21 y su uso genera advertencias.");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            System.out.println("SecurityManager instalado (estilo Java obsoleto).");
        }
    }
}
