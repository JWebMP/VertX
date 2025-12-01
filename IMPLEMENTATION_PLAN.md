# Implementation Plan (Forward-Only)

Scope: Apply remaining adoption requirements with minimal scaffolding; no source logic changes expected because implementations ship from upstream artifacts.

## Planned Changes
1. README refresh
   - Note Rules Repository submodule usage and link PACT, RULES, GUIDES, IMPLEMENTATION, GLOSSARY, architecture index, PROMPT_REFERENCE.
2. Environment sample
   - Add `.env.example` aligned to `rules/generative/platform/secrets-config/env-variables.md`; capture placeholders for Vert.x/JWebMP runtime where applicable.
3. CI/CD
   - Add `.github/workflows/maven-package.yml` referencing GuicedEE shared workflow with required secrets.
4. AI workspace files
   - `.aiassistant/rules/` summary of RULES sections 4/5 + Document Modularity + Forward-Only.
   - `.github/copilot-instructions.md` mirroring constraints and STOP-gate policy.
   - `.cursor/rules.md` mirroring constraints (Cursor selected).
5. Submodule hygiene
   - Ensure `rules/` treated as submodule; no host docs placed inside.
6. Migration/risk notes
   - Record forward-only removals/assumptions in `MIGRATION.md` if any docs are replaced.

## Validation
- Links resolve to submodule rules and host docs.
- Workflow lints run via shared GitHub Action when pushed.
- No changes to `src/main/java` expected; module-info remains source of truth until new code is added.
