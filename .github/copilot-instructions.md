# Copilot Workspace Instructions — JWebMP Vert.X

- Load host `RULES.md` plus `rules/RULES.md` sections 4/5, Document Modularity Policy, and Forward-Only Change Policy before generating code.
- Respect stage-gated workflow (PACT → RULES → GUIDES → IMPLEMENTATION; blanket approval noted in `PACT.md`). Do not modify source until docs are aligned.
- Use Java 25 + Maven; enforce CRTP fluent style (no @Builder) for GuicedEE/JWebMP components.
- Logging defaults to Log4j2; align examples accordingly.
- Keep project docs outside `rules/`; update all links when files move (no legacy anchors).
- Maintain traceability across PACT, GLOSSARY, RULES, GUIDES, IMPLEMENTATION, PROMPT_REFERENCE, and architecture diagrams in `docs/architecture/`.
