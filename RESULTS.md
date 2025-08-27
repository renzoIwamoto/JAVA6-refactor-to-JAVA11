
# RESULTS

| Herramienta | Compila migración (S/N) | Iteraciones | Tiempo (min) | Warnings | Modernización (checklist %) | Tests OK (%) | Cobertura | Notas/Halluc. |
|---|---:|---:|---:|---:|---:|---:|---:|---|
| Copilot |  |  |  |  |  |  |  |  |
| Amazon Q |  |  |  |  |  |  |  |  |
| Claude Code |  |  |  |  |  |  |  |  |

### Checklist de modernización (esperado)
- [ ] `List<Task>` en lugar de raw types; `var` donde tenga sentido.
- [ ] `java.time` en vez de `Date/Calendar`.
- [ ] `List.copyOf` o colecciones inmutables.
- [ ] `Stream` + `forEach` y `Collectors.toList()`.
- [ ] `record Task(...)` (opcional si decides mutar el modelo).
- [ ] Concurrencia moderna si aplica: `CopyOnWriteArrayList`, `ConcurrentLinkedQueue`, etc.
- [ ] `Optional` en vez de `null` donde aplique.
- [ ] Lint estático limpio (SpotBugs/Checkstyle).
