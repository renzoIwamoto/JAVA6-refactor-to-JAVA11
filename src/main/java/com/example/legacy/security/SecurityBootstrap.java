package com.example.legacy.security;

import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.PropertyPermission;

public class SecurityBootstrap {
    
    public static void installSecurityManager() {
        try {
            System.out.println("=== INSTALLING LEGACY SECURITY MANAGER ===");
            
            System.setProperty("java.security.policy", "security/legacy.policy");
            
            SecurityManager securityManager = new LegacySecurityManager();
            System.setSecurityManager(securityManager);
            
            System.out.println("✓ SecurityManager installed successfully");
            System.out.println("✓ Policy file: security/legacy.policy");
            System.out.println("WARNING: SecurityManager is DEPRECATED in Java 17+");
            System.out.println("WARNING: SecurityManager is DISABLED by default in Java 21+");
            System.out.println("Migration: Remove SecurityManager usage");
            System.out.println("=========================================");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to install SecurityManager: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void checkSecurityManagerStatus() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            System.out.println("✓ SecurityManager is active: " + sm.getClass().getName());
            
            try {
                sm.checkPermission(new FilePermission("test.txt", "read"));
                System.out.println("✓ File read permission check passed");
            } catch (SecurityException e) {
                System.out.println("✗ File read permission denied: " + e.getMessage());
            }
            
            try {
                sm.checkPermission(new SocketPermission("localhost:8080", "listen"));
                System.out.println("✓ Socket listen permission check passed");
            } catch (SecurityException e) {
                System.out.println("✗ Socket listen permission denied: " + e.getMessage());
            }
            
        } else {
            System.out.println("ⓘ No SecurityManager is installed");
        }
    }
    
    private static class LegacySecurityManager extends SecurityManager {
        
        public void checkPermission(Permission perm) {
            if (perm instanceof RuntimePermission) {
                String name = perm.getName();
                if (name != null && name.startsWith("setSecurityManager")) {
                    return;
                }
            }
            
            if (perm instanceof PropertyPermission) {
                PropertyPermission propPerm = (PropertyPermission) perm;
                if ("read".equals(propPerm.getActions()) || "write".equals(propPerm.getActions())) {
                    return;
                }
            }
            
            if (perm instanceof FilePermission) {
                FilePermission filePerm = (FilePermission) perm;
                String actions = filePerm.getActions();
                if (actions != null && (actions.contains("read") || actions.contains("write"))) {
                    String path = filePerm.getName();
                    if (path != null && (path.endsWith(".xml") || path.endsWith(".policy") || 
                                       path.contains("temp") || path.contains("target"))) {
                        return;
                    }
                }
            }
            
            if (perm instanceof SocketPermission) {
                SocketPermission sockPerm = (SocketPermission) perm;
                String actions = sockPerm.getActions();
                if (actions != null && (actions.contains("listen") || actions.contains("connect"))) {
                    return;
                }
            }
            
            Policy policy = Policy.getPolicy();
            if (policy != null) {
                ProtectionDomain domain = getClass().getProtectionDomain();
                if (policy.implies(domain, perm)) {
                    return;
                }
            }
            
            super.checkPermission(perm);
        }
        
        public void checkRead(String file) {
            if (file != null && (file.endsWith(".xml") || file.endsWith(".policy") || 
                               file.contains("target") || file.contains("classes"))) {
                return;
            }
            super.checkRead(file);
        }
        
        public void checkWrite(String file) {
            if (file != null && (file.contains("temp") || file.contains("target") || 
                               file.endsWith(".xml") || file.endsWith(".log"))) {
                return;
            }
            super.checkWrite(file);
        }
        
        public void checkListen(int port) {
            if (port >= 8080 && port <= 8090) {
                return;
            }
            super.checkListen(port);
        }
        
        public void checkConnect(String host, int port) {
            if (host != null && (host.equals("localhost") || host.equals("127.0.0.1") || 
                               host.equals("0.0.0.0"))) {
                return;
            }
            super.checkConnect(host, port);
        }
    }
}