# Legacy Orders Java 6 Project

## 🎯 Objetivo

Este proyecto demuestra un sistema Maven completo desarrollado en Java 6 que **funciona perfectamente** en Java 6 pero **se rompe deliberadamente** en versiones modernas de Java (11/17/21) debido a dependencias y características que fueron removidas o cambiaron.

**Caso de uso real**: Sistema de gestión de órdenes con arquitectura por capas, servicios SOAP, marshalling XML y características típicas de aplicaciones Java 6 empresariales.

## 🏗️ Arquitectura del Proyecto

```
legacy-orders-java6/
├── README.md                     # Este archivo
├── pom.xml                       # Maven configurado para Java 6
├── docker/
│   └── Dockerfile.jdk6           # Imagen Docker con JDK 6 + Maven 3.2.5
├── scripts/
│   ├── build_java6.sh           # Script de construcción
│   ├── run_java6.sh             # Script de ejecución con flags legacy
│   └── soap_test_curl.sh        # Tests del servicio SOAP
├── security/
│   └── legacy.policy            # Archivo de políticas de seguridad
└── src/main/java/com/example/legacy/
    ├── app/App.java             # Punto de entrada principal
    ├── model/                   # Modelos de dominio
    │   ├── Customer.java
    │   └── Order.java
    ├── repository/              # Capa de persistencia (in-memory)
    │   └── OrderRepository.java
    ├── service/                 # Lógica de negocio
    │   └── OrderService.java
    ├── legacy/                  # Características legacy que se rompen
    │   ├── XmlMarshaller.java      # JAXB javax.xml.bind
    │   ├── LegacyBase64.java       # sun.misc.BASE64*
    │   ├── LegacySoapService.java  # JAX-WS javax.jws.*
    │   └── LegacySoapEndpoint.java # javax.xml.ws.Endpoint
    └── security/
        └── SecurityBootstrap.java  # SecurityManager legacy
```

## 🚨 Características Legacy que se Rompen

### 1. **JAXB en el JDK** (`javax.xml.bind`)
- **Java 6**: ✅ Incluido en el JDK por defecto
- **Java 11+**: ❌ **REMOVIDO** del JDK
- **Ubicación**: `src/main/java/com/example/legacy/legacy/XmlMarshaller.java`
- **Error en Java 11+**: `ClassNotFoundException: javax.xml.bind.JAXBContext`

### 2. **JAX-WS SOAP** (`javax.xml.ws`)
- **Java 6**: ✅ Incluido en el JDK por defecto
- **Java 11+**: ❌ **REMOVIDO** del JDK
- **Ubicación**: `src/main/java/com/example/legacy/legacy/LegacySoapService.java`
- **Error en Java 11+**: `ClassNotFoundException: javax.xml.ws.Endpoint`

### 3. **sun.misc.BASE64Encoder/Decoder**
- **Java 6**: ✅ Disponible (aunque no oficial)
- **Java 9+**: ❌ **ENCAPSULADO/REMOVIDO**
- **Ubicación**: `src/main/java/com/example/legacy/legacy/LegacyBase64.java`
- **Error en Java 9+**: `IllegalAccessError` o `ClassNotFoundException`

### 4. **SecurityManager + Policy**
- **Java 6**: ✅ Funcional y ampliamente usado
- **Java 17**: ⚠️ **DEPRECADO** para eliminación
- **Java 21**: ❌ **DESHABILITADO** por defecto
- **Ubicación**: `src/main/java/com/example/legacy/security/SecurityBootstrap.java`
- **Error en Java 21+**: `UnsupportedOperationException` si no se habilita explícitamente

### 5. **Flags JVM de PermGen**
- **Java 6/7**: ✅ `-XX:PermSize=64m -XX:MaxPermSize=128m`
- **Java 8+**: ❌ **PermGen eliminado**, reemplazado por Metaspace
- **Ubicación**: `scripts/run_java6.sh`
- **Error en Java 8+**: `Unrecognized VM option`

## 🐳 Instalación y Ejecución

### Requisitos Previos
- Docker
- Bash (Git Bash en Windows)
- curl (para tests SOAP)

### 1. Construir el Proyecto

```bash
# Construye imagen Docker con JDK 6 + Maven 3.2.5 y compila el proyecto
./scripts/build_java6.sh
```

### 2. Ejecutar la Aplicación

```bash
# Ejecuta con flags JVM legacy que fallan en Java moderno
./scripts/run_java6.sh
```

### 3. Probar el Servicio SOAP

```bash
# Tests automáticos del endpoint SOAP (en otra terminal)
./scripts/soap_test_curl.sh
```

## 🔍 Funcionalidades Demonstradas

### Gestión de Órdenes
- Crear órdenes con clientes y productos
- Procesar y completar órdenes
- Buscar órdenes por estado, cliente, fecha
- Calcular ingresos totales
- Generar reportes

### Serialización XML (JAXB)
- Marshall/Unmarshall de objetos Order a XML
- **Usa**: `javax.xml.bind` (incluido en Java 6)
- **Rompe**: Java 11+ (javax.xml.bind removido)

### Codificación Base64 Legacy
- Codificar/decodificar contenido XML y texto
- **Usa**: `sun.misc.BASE64Encoder/BASE64Decoder`
- **Rompe**: Java 9+ (sun.misc encapsulado)

### Servicio SOAP
- Endpoint SOAP con operaciones CRUD
- **Usa**: `javax.xml.ws.Endpoint.publish()` (incluido en Java 6)
- **Rompe**: Java 11+ (javax.xml.ws removido)
- **URL**: http://localhost:8080/legacy
- **WSDL**: http://localhost:8080/legacy?wsdl

### Security Manager
- Política de seguridad personalizada
- **Usa**: `System.setSecurityManager()` (Java 6)
- **Rompe**: Java 21+ (deshabilitado por defecto)

## 🚫 Cómo Verificar la Ruptura en JDK Moderno

### Con Java 21 Local (sin Docker):

```bash
# 1. Compilar (debería fallar por dependencias faltantes)
mvn clean compile
# Error: package javax.xml.bind does not exist

# 2. Si tuvieras las dependencias, ejecutar con flags legacy fallaría:
java -XX:PermSize=64m -XX:MaxPermSize=128m -jar target/legacy-orders-java6.jar
# Error: Unrecognized VM option 'PermSize'

# 3. SecurityManager en Java 21 requiere habilitarlo explícitamente:
java -Djava.security.manager=allow -jar target/legacy-orders-java6.jar
# Warning: SecurityManager is deprecated and will be removed
```

## 🔧 Checklist de Migración a Java 21

### 1. **Reemplazar JAXB**
```xml
<!-- Agregar al pom.xml -->
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.0</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.0</version>
    <scope>runtime</scope>
</dependency>
```
```java
// Cambiar imports
import jakarta.xml.bind.JAXBContext;  // era javax.xml.bind
```

### 2. **Reemplazar JAX-WS**
```xml
<!-- Agregar al pom.xml -->
<dependency>
    <groupId>com.sun.xml.ws</groupId>
    <artifactId>jaxws-rt</artifactId>
    <version>4.0.0</version>
</dependency>
```

### 3. **Reemplazar Base64 Legacy**
```java
// Era: sun.misc.BASE64Encoder
import java.util.Base64;

// Nuevo código:
String encoded = Base64.getEncoder().encodeToString(data);
byte[] decoded = Base64.getDecoder().decode(encoded);
```

### 4. **Eliminar SecurityManager**
```java
// Remover completamente:
// System.setSecurityManager(securityManager);

// Migrar a alternativas modernas:
// - Spring Security
// - Controles a nivel de aplicación
// - Containerización (Docker)
```

### 5. **Actualizar Flags JVM**
```bash
# Remover flags de PermGen:
# -XX:PermSize=64m -XX:MaxPermSize=128m

# Agregar flags de Metaspace si necesario:
-XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m
```

### 6. **Migrar Fechas a java.time**
```java
// Era: java.util.Date, Calendar
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

// Nuevo código:
LocalDateTime now = LocalDateTime.now();
```

### 7. **Actualizar Collections**
```java
// Era: Collections.synchronizedList()
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Usar colecciones concurrentes modernas
```

## 📊 Comparación de Versiones

| Característica | Java 6 | Java 11 | Java 17 | Java 21 |
|----------------|--------|---------|---------|---------|
| JAXB (`javax.xml.bind`) | ✅ Incluido | ❌ Removido | ❌ Removido | ❌ Removido |
| JAX-WS (`javax.xml.ws`) | ✅ Incluido | ❌ Removido | ❌ Removido | ❌ Removido |
| `sun.misc.BASE64*` | ✅ Disponible | ❌ Encapsulado | ❌ Encapsulado | ❌ Encapsulado |
| SecurityManager | ✅ Activo | ✅ Activo | ⚠️ Deprecado | ❌ Deshabilitado |
| Flags PermGen | ✅ Válidos | ❌ Error | ❌ Error | ❌ Error |
| `java.util.Date` | ✅ Principal | ⚠️ Legacy | ⚠️ Legacy | ⚠️ Legacy |

## 🎯 Resultados Esperados

### En Java 6 (Docker):
```
✅ Aplicación inicia correctamente
✅ SecurityManager instalado
✅ JAXB marshalling funcional
✅ Base64 encoding funcional  
✅ SOAP service publicado en puerto 8080
✅ Todas las operaciones funcionan
```

### En Java 21 (sin migración):
```
❌ Error de compilación: javax.xml.bind no existe
❌ Error de compilación: javax.xml.ws no existe  
❌ Error de compilación: sun.misc.BASE64Encoder no accesible
❌ Error JVM: PermSize no reconocido
❌ Warning: SecurityManager deprecado
```

## 🔗 Enlaces Útiles

- [JEP 261: Module System](https://openjdk.org/jeps/261) - Modularización que removió APIs
- [JEP 320: Remove Java EE and CORBA Modules](https://openjdk.org/jeps/320) - Remoción de JAXB/JAX-WS
- [JEP 396: Strongly Encapsulate JDK Internals](https://openjdk.org/jeps/396) - Encapsulación sun.misc
- [JEP 411: Deprecate SecurityManager](https://openjdk.org/jeps/411) - Deprecación SecurityManager

## 📝 Conclusión

Este proyecto demuestra de forma práctica cómo aplicaciones Java 6 reales **dependen de características que ya no existen** en versiones modernas de Java. Representa un escenario común en empresas con aplicaciones legacy que requieren migración cuidadosa y planificada.

**Moraleja**: La migración de Java 6 a versiones modernas no es solo cambiar la versión del JDK, sino una refactorización integral que debe planificarse con cuidado, probarse exhaustivamente, y ejecutarse por fases.
