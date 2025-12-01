# GUIDES — How to Apply Rules

## Language & Build (Java 25 + Maven)
- Follow `rules/generative/language/java/java-25.rules.md` for toolchains, JPMS, and CRTP patterns.
- Build wiring and plugins: `rules/generative/language/java/build-tooling.md`.

## Backend Reactive (Vert.x + GuicedEE)
- Vert.x hosting and routing: `rules/generative/backend/vertx/README.md`.
- GuicedEE integration: `rules/generative/backend/guicedee/README.md`, with Vert.x bridge specifics at `rules/generative/backend/guicedee/vertx/README.md`.
- Client/Web modules for GuicedEE: `rules/generative/backend/guicedee/client/README.md`, `rules/generative/backend/guicedee/web/README.md`.
- Fluent API CRTP patterns: `rules/generative/backend/fluent-api/GLOSSARY.md`.

## Frontend (JWebMP Client)
- Component and rendering rules: `rules/generative/frontend/jwebmp/README.md`, `rules/generative/frontend/jwebmp/client/README.md`.
- Align any WebAwesome additions via `rules/generative/frontend/webawesome/README.md` if introduced later.

## Testing & Quality
- Docs-first, test-first flow: `rules/generative/architecture/tdd/README.md`.
- Add Maven test scopes under `src/test/java`; reuse JWebMP testlib (`pom.xml`).

## CI/CD and Environments
- GitHub Actions workflow guidance: `rules/generative/platform/ci-cd/providers/github-actions.md`.
- Environment variable guidance: `rules/generative/platform/secrets-config/env-variables.md` (align with `.env.example`).

## Architecture Artifacts
- Architecture index and diagrams live in `docs/architecture/README.md` (Mermaid sources).
- Keep GUIDES ↔ IMPLEMENTATION aligned when adding modules or routes.
