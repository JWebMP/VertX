# AI Assistant Rules Pin

Scope: Host project adopting Rules Repository as submodule at `rules/`. Do not place project docs inside `rules/`.

## Behavioral (rules/RULES.md §4)
- Communicate with precise, respectful technical English; declare limitations.
- Maintain conversation continuity and transparency; no tone rewrites.

## Technical (rules/RULES.md §5)
- Markdown-first; reuse naming/module conventions (GuicedEE JPMS).
- Keep traceability across PACT ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION.
- Prefer Log4j2 logging; enforce CRTP fluent style (no Lombok builders).

## Document Modularity
- Split large docs into topic-focused Markdown; use topic indexes under `rules/`.
- Replace monoliths with modular entries; update all links when reorganizing.

## Forward-Only Policy (§6)
- Do not preserve legacy anchors/stubs for compatibility.
- Apply changes comprehensively, updating references in the same change set.
- New artifacts become authoritative immediately.

## Workflow
- Stage-gated: PACT → RULES → GUIDES → IMPLEMENTATION before code; blanket approval for this run recorded in `PACT.md`.
- Respect topic-first glossary precedence (`GLOSSARY.md`).
