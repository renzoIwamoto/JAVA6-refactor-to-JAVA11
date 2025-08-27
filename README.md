# legacy-orders-java6 — Proyecto Java 6 (con rasgos que rompen en JDK modernos)

Proyecto **Maven** en **Java 6** que **compila y corre en JDK 6** y contiene características/dependencias
**propias de Java 6** que **no funcionan** o **cambian** en **Java 11/17/21** sin refactor:
- JAXB del **JDK** (`javax.xml.bind`) — **removido** del JDK en Java 11+
- JAX-WS del **JDK** (`javax.jws.*`, `javax.xml.ws.*`) — **removido** en Java 11+
- `sun.misc.BASE64Encoder`/`BASE64Decoder` — **encapsulados/removidos** en Java 9+
- **SecurityManager** + policy — **deprecado** en 17 y **deshabilitado** en 21
- Flags **PermGen** (`-XX:PermSize`, `-XX:MaxPermSize`) — **eliminadas** en Java 8+

> Este repo es ideal para ejercicios de migración Java 6 → Java 21 (Copilot / Amazon Q / Claude Code).

## Compilar
```bash
chmod +x scripts/*.sh
./scripts/build_java6.sh
```
## Ejecutar
```bash
./scripts/run_java6.sh
./scripts/soap_test_curl.sh
```
