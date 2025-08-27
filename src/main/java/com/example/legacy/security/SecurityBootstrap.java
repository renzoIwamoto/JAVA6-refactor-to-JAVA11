package com.example.legacy.security;

public class SecurityBootstrap {
    public static void install() {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                System.out.println("SecurityManager instalado (Java 6 style).");
            }
        } catch (Throwable t) {
            System.out.println("No se pudo instalar SecurityManager: " + t);
        }
    }
}
