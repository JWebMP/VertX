# VertX

Vert.x 5 bridge for JWebMP using GuicedEE modules (`com.jwebmp.vertx`).

## Documentation
- PACT: `PACT.md`
- RULES: `RULES.md`
- GUIDES: `GUIDES.md`
- IMPLEMENTATION: `IMPLEMENTATION.md`
- GLOSSARY: `GLOSSARY.md`
- Architecture diagrams: `docs/architecture/README.md`
- Prompt reference: `docs/PROMPT_REFERENCE.md`
- Rules Repository submodule: `rules/README.md`

## Build
- Java 25 LTS, Maven (inherits `com.jwebmp:parent:2.0.0-SNAPSHOT`).
- JPMS module descriptor at `src/main/java/module-info.java`.

## CI
- GitHub Actions workflow at `.github/workflows/maven-package.yml` (GuicedEE shared workflow). Configure secrets: `USERNAME`, `USER_TOKEN`, `SONA_USERNAME`, `SONA_PASSWORD`.
