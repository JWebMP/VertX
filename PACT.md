# PACT — JWebMP Vert.X Integration

## Purpose and Scope
- Deliver a Vert.x 5 aligned JWebMP bridge packaged as `com.jwebmp.vertx` for GuicedEE projects.
- Maintain forward-only documentation alignment with the Rules Repository submodule at `rules/`.
- Stack pin: Java 25 LTS, Maven build, CRTP fluent strategy (enforced by GuicedEE/JWebMP selection), Log4j2 logging policy.

## Outcomes
- JWebMP applications can be hosted on Vert.x HTTP with Guice modules provided by this library.
- Documentation-first workflow: PACT → RULES → GUIDES → IMPLEMENTATION with topic-first GLOSSARY.
- CI uses GitHub Actions (shared workflow) for build/test; environment variables documented in `.env.example`.

## Non-Goals
- Define business domain models or persistence (none present in the codebase).
- Replace upstream GuicedEE/Vert.x configuration or provide alternate servers.

## Constraints and Policies
- Follow RULES.md sections 4/5, Document Modularity Policy, and Forward-Only Change Policy from `rules/RULES.md`.
- Do not place host project docs inside `rules/`; host docs live at repository root or under `docs/`.
- CRTP fluent APIs where applicable; avoid Lombok builders.
- Log4j2 as default logging strategy when examples are added.

## Interfaces and Integrations
- Provides `IGuiceModule` and `VertxHttpServerConfigurator` implementations (see `src/main/java/module-info.java`).
- Depends on `jwebmp-client`, `jwebmp-core`, `guiced-vertx-web`, `vertx-web`, `vertx-core`, and GuicedEE JSON representation libraries (see `pom.xml`).

## Risks and Assumptions
- Limited source coverage: only JPMS module descriptor available; deeper component behavior inferred from dependencies.
- No persistence or messaging observed; ERD captures runtime wiring only.
- Requires Java 25 toolchain and Maven settings consistent with parent `com.jwebmp:parent:2.0.0-SNAPSHOT`.

## Traceability Links
- Rules Repository submodule index: `rules/README.md`
- Project RULES: `RULES.md` (host)
- GUIDES: `GUIDES.md`
- IMPLEMENTATION: `IMPLEMENTATION.md`
- GLOSSARY: `GLOSSARY.md`
- Architecture index and diagrams: `docs/architecture/README.md`
- Prompt and stack reference: `docs/PROMPT_REFERENCE.md`

