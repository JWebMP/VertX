# Cursor Rules — JWebMP Vert.X

- Pin `RULES.md` (host) and `rules/RULES.md` sections 4/5 plus Document Modularity and Forward-Only policies.
- Follow stage sequence: PACT → RULES → GUIDES → IMPLEMENTATION before editing code (blanket approval recorded in `PACT.md`).
- Stack: Java 25 + Maven, CRTP fluent APIs (no Lombok builders), Log4j2 logging default.
- Use topic-first glossaries (`GLOSSARY.md`) with precedence for Java, Vert.x/GuicedEE, JWebMP.
- Keep docs outside `rules/`; update links/indexes when reorganizing (no legacy anchors).
- Reference architecture diagrams under `docs/architecture/` when generating or reviewing flows.
