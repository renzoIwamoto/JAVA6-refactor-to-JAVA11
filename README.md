
# Java 6 → Java 21 Refactor Experiment

Este repositorio está listo para realizar un experimento controlado comparando **GitHub Copilot**, **Amazon Q** y **Claude Code** para:
1) Migrar un proyecto **Java 6** a **Java 21** con el *mismo prompt*.
2) Generar **pruebas unitarias** en Java 21 (JUnit 5 + Mockito) con el *mismo prompt*.

## Estructura
```
java6-to-java21/
├─ README.md
├─ RESULTS.md
├─ scripts/
│  ├─ build_java6.sh
│  ├─ run_java6.sh
│  ├─ build_java21.sh
│  └─ run_java21.sh
├─ docker/
│  └─ Dockerfile.jdk6
├─ baseline-java6/
│  ├─ pom.xml
│  └─ src/
│     ├─ main/java/com/example/legacy/...
│     └─ main/resources/.gitkeep
└─ java21-ci/
   └─ .github/workflows/build-java21.yml
```

## Uso rápido

### 0) Requisitos
- Docker (para compilar Java 6 sin instalar un JDK antiguo en tu host)
- Maven y JDK 21 para la fase migrada

### 1) Compilar/ejecutar Java 6 (baseline)
```bash
chmod +x scripts/*.sh
./scripts/build_java6.sh
./scripts/run_java6.sh
```

### 2) Migrar a Java 21 (por herramienta)
Crea una rama por herramienta (ej.: `copilot-java21`, `amazonq-java21`, `claudecode-java21`) y aplica el **mismo prompt** (ver abajo). Compila con:
```bash
./scripts/build_java21.sh
./scripts/run_java21.sh
```

### 3) Generar pruebas unitarias (Java 21)
En cada rama migrada, aplica el **mismo prompt** de tests y ejecuta:
```bash
mvn -q test
```

## Prompts estandarizados

### Prompt de migración a Java 21
> Toma este repositorio cuyo módulo `baseline-java6` es Java 6.  
> Objetivo: migrarlo a **Java 21** sin cambiar la lógica de negocio.  
> **Requisitos**:
> 1) Actualiza `pom.xml` a Java 21 (maven-compiler-plugin), mantén Maven simple.  
> 2) Reemplaza `Date/Calendar` por `java.time` (por ejemplo, `Instant`/`LocalDateTime`).  
> 3) Reemplaza raw types por genéricos y usa `List.copyOf`/inmutabilidad cuando aplique.  
> 4) Moderniza bucles a `Streams` donde mejore la claridad.  
> 5) Evalúa `synchronizedList`: si no hay multi-hilo, elimínalo; si sí, usa alternativa moderna.  
> 6) No introduzcas frameworks adicionales, solo JDK estándar.  
> Entrega: código compilable en Java 21 y breve diff/resumen de cambios.

### Prompt de generación de pruebas unitarias (Java 21)
> Ahora genera **pruebas unitarias con JUnit 5** y **Mockito** (si aplica) para el código migrado a Java 21.  
> **Requisitos**:
> 1) Cubre `TaskService` y `TaskStore` con casos felices y bordes (lista vacía, múltiples tareas).  
> 2) Asegura que el test **no depende de la hora del sistema** (inyecta un `Clock` o simula la fecha).  
> 3) Agrega `pom.xml` con dependencias JUnit 5 y Mockito.  
> 4) Incluye aserciones claras y nombra los tests con Given/When/Then.  
> 5) Apunta a **>80% de cobertura**.  
> Entrega: tests que compilen y pasen.

## CI (Java 21)
Se incluye un workflow mínimo en `java21-ci/.github/workflows/build-java21.yml` para compilar y testear las ramas migradas.

## Métricas (RESULTS.md)
Rellena la tabla comparando las herramientas en compilación, warnings, modernización, cobertura, etc.
