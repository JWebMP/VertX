# Prompt Reference — JWebMP Vert.X

- Repository: https://github.com/JWebMP/VertX.git (local path `/mnt/c/Java/DevSuite/JWebMP/vertx`)
- Org/Project: JWebMP / Vert.X
- Description: Configures the GuicedEE Vert.x library for JWebMP usage.
- License: Apache 2 (per prompt; verify upstream parent).

## Stack Selections
- Language: Java 25 LTS
- Build: Maven
- Architectures: Specification-Driven Design, Documentation-as-Code, TDD (docs/test-first), CRTP fluent strategy
- Reactive backend: Vert.x 5 + GuicedEE (Core/Client/Web)
- Frontend: JWebMP Client
- CI/CD: GitHub Actions (GuicedEE shared workflow)
- Logging: Log4j2 default policy

## Rules References
- Root rules: `rules/RULES.md` (Sections 4/5, Document Modularity Policy, Forward-Only)
- Java 25 rules: `rules/generative/language/java/java-25.rules.md`
- Build tooling: `rules/generative/language/java/build-tooling.md`
- Vert.x rules: `rules/generative/backend/vertx/README.md`
- GuicedEE rules: `rules/generative/backend/guicedee/README.md`, `rules/generative/backend/guicedee/client/README.md`, `rules/generative/backend/guicedee/web/README.md`
- JWebMP rules: `rules/generative/frontend/jwebmp/README.md`, `rules/generative/frontend/jwebmp/client/README.md`
- CI/CD provider: `rules/generative/platform/ci-cd/providers/github-actions.md`
- Secrets/env: `rules/generative/platform/secrets-config/env-variables.md`

## Diagram Links
- Architecture index: `docs/architecture/README.md`
- Context: `docs/architecture/c4-context.md`
- Container: `docs/architecture/c4-container.md`
- Component: `docs/architecture/c4-component-vertx-bridge.md`
- Sequences: `docs/architecture/sequence-http-request.md`, `docs/architecture/sequence-startup.md`
- ERD: `docs/architecture/erd-runtime-wiring.md`

Use this reference to seed future prompts; ensure PACT ↔ GLOSSARY ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION stay synchronized.
