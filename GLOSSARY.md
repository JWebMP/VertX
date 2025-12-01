# GLOSSARY — Topic-First Index

Glossary Precedence Policy: Topic glossaries override root definitions within their scope. This host glossary is an index; avoid duplicating terms. Copy enforced Prompt Language Alignment mappings only when mandated by a topic (none selected yet). When ambiguity arises, prefer the most specific topic glossary before falling back to this index or enterprise root `rules/GLOSSARY.md`.

## Selected Topic Glossaries
- Java (language, Java 25): `rules/generative/language/java/GLOSSARY.md`
- Fluent API (CRTP): `rules/generative/backend/fluent-api/GLOSSARY.md`
- Vert.x with GuicedEE bridge: `rules/generative/backend/guicedee/vertx/GLOSSARY.md`
- GuicedEE Core: `rules/generative/backend/guicedee/GLOSSARY.md`
- GuicedEE Client/Web: `rules/generative/backend/guicedee/client/GLOSSARY.md`, `rules/generative/backend/guicedee/web/GLOSSARY.md`
- JWebMP Client: `rules/generative/frontend/jwebmp/client/GLOSSARY.md`

## Host-Specific Notes
- Fluent API strategy: CRTP — fluent setters must return the concrete subtype (suppress unchecked casts where required).
- Logging policy: Log4j2 terminology; align examples with Log4j2 appenders/layouts when added.
- No WebAwesome alignment selected; add mappings (WaButton/WaInput/WaCluster/WaStack) only if the plugin is introduced.
