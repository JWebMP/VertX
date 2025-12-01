# Project RULES — JWebMP Vert.X

Scope: Host repository consuming Rules Repository submodule at `rules/`. Forward-only, document-first, no project docs inside submodule.

## Governance and Workflow
- Honor `rules/RULES.md` sections 4/5 plus Document Modularity and Forward-Only policies.
- Stage-gated flow: PACT → RULES → GUIDES → IMPLEMENTATION before source changes; blanket approval noted in PROMPT inputs.
- Logging default: Log4j2; prefer Lombok `@Log4j2` only if Lombok is present (not currently used).
- Fluent API strategy: CRTP enforced (GuicedEE/JWebMP). Avoid Lombok builders; return `(J)this` from fluent setters when added.

## Selected Stacks and References
- Language/Build: Java 25 LTS + Maven — see `rules/generative/language/java/java-25.rules.md` and `rules/generative/language/java/build-tooling.md`.
- Backend Reactive: Vert.x 5 + GuicedEE (Core/Client/Web) — `rules/generative/backend/vertx/README.md`, `rules/generative/backend/guicedee/README.md`, `rules/generative/backend/guicedee/client/README.md`, `rules/generative/backend/guicedee/web/README.md`, `rules/generative/backend/guicedee/vertx/README.md`.
- Frontend Framework: JWebMP Client — `rules/generative/frontend/jwebmp/README.md`, `rules/generative/frontend/jwebmp/client/README.md`.
- Architecture: SDD, Docs-as-Code, TDD — `rules/generative/architecture/tdd/README.md`, `rules/generative/architecture/README.md`.
- CI/CD: GitHub Actions — `rules/generative/platform/ci-cd/README.md`, provider guide `rules/generative/platform/ci-cd/providers/github-actions.md`.
- Secrets/Env: `rules/generative/platform/secrets-config/env-variables.md`.

## Glossary Alignment
- Topic-first precedence; host `GLOSSARY.md` indexes topic glossaries (Java, Vert.x, GuicedEE, JWebMP). Topic terms override root scope.
- Apply Prompt Language Alignment mappings from selected topics when present.

## Repository Boundaries
- Host code and docs live at repository root or under `docs/`.
- Do not duplicate or edit rules within `rules/`; extend via this file and GUIDES/IMPLEMENTATION.
- Keep architecture diagrams under `docs/architecture/` with Mermaid sources; update indexes when diagrams change.

## Testing and Quality
- Target Maven test lifecycle; add/extend tests under `src/test/java` when code is added.
- Enforce CRTP style and JPMS module correctness before release.

## Traceability
- Maintain cross-links: PACT ↔ GLOSSARY ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION ↔ PROMPT_REFERENCE. Update all links on change per forward-only policy.
