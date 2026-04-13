# JWebMP Vert.X

[![Build](https://github.com/JWebMP/VertX/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/JWebMP/VertX/actions/workflows/maven-package.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.jwebmp/jwebmp-vertx)](https://central.sonatype.com/artifact/com.jwebmp/jwebmp-vertx)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](https://www.apache.org/licenses/LICENSE-2.0)

![Java 25+](https://img.shields.io/badge/Java-25%2B-green)
![Vert.X 5](https://img.shields.io/badge/Vert.x-5%2B-green)
![Maven 4](https://img.shields.io/badge/Maven-4%2B-green)

A portable connector between **JWebMP** and **Vert.x 5**, powered by [GuicedEE](https://github.com/GuicedEE).  
Registers HTTP routes for page rendering, CSS delivery, AJAX event handling, data components, and internal scripts — all inside the GuicedEE call-scope model. WebSocket broadcasting is bridged through the Vert.x event bus (with optional STOMP destinations).

## ✨ Features

- **Automatic page routing** — classes annotated with `@PageConfiguration` are discovered at startup and served as Vert.x routes
- **AJAX event pipeline** — fully reactive request → deserialise → intercept → `fireEvent()` → JSON response, with 10-second timeout and structured error handling
- **Data component servlet** — serves `IDataComponent.renderData()` results as JSON
- **CSS endpoint** — renders page-level CSS on demand
- **Site-loader script** — template-driven JS bootstrap (`siteloader.js`) with server address, page class, user-agent, and referrer placeholders
- **WebSocket broadcasting** — `IGuicedWebSocket` bridged to the Vert.x event bus (direct or STOMP prefix)
- **User-agent detection** — `ReadableUserAgent` provided per call-scope via UADetector
- **Call-scope integration** — every HTTP handler enters a `CallScope`, populating `CallScopeProperties` with `RoutingContext`, request, response, and stream ID
- **Jackson configuration** — Vert.x `DatabindCodec` mapper aligned with GuicedEE JSON conventions (quoted field names, single-quote tolerance)

## 📦 Installation

```xml
<dependency>
  <groupId>com.jwebmp</groupId>
  <artifactId>jwebmp-vertx</artifactId>
</dependency>
```

> Version is managed by the `com.jwebmp:jwebmp-bom` imported in the parent POM.

## 🗺️ JPMS Module

```
module com.jwebmp.vertx {
    requires transitive com.jwebmp.client;
    requires transitive com.guicedee.vertx.web;
    requires transitive com.jwebmp.core;
    requires transitive com.guicedee.guicedinjection;
    requires transitive com.guicedee.jsonrepresentation;

    provides IGuiceModule      with JWebMPVertx, JWebMPVertxBinder;
    provides VertxHttpServerConfigurator with JWebMPVertx;
}
```

## 🚀 Quick Start

No manual registration is needed — the module is discovered automatically via SPI when it appears on the module path.

1. Annotate a page class:

```java
@PageConfiguration(url = "/")
public class HomePage extends Page<HomePage> { }
```

2. Start GuicedEE with Vert.x:

```java
IGuiceContext.instance().inject();   // discovers JWebMPVertx via SPI
```

3. Vert.x routes are registered automatically:
   - `GET /` — serves `HomePage`
   - `GET /jwscr` — site-loader bootstrap script
   - `POST /jwajax` — AJAX event receiver
   - `GET /jwdata` — data component endpoint
   - `GET /jwcss` — CSS endpoint

## 🏗️ Architecture

### HTTP Routes

| Route | Method | Handler | Purpose |
|---|---|---|---|
| `@PageConfiguration.url()` | GET | `configurePageServlet` | Renders the annotated `IPage` as HTML |
| `/jwajax` | POST | `configureAjaxReceiveServlet` | Processes AJAX event calls reactively |
| `/jwdata` | GET | `configureDataServlet` | Serves `IDataComponent` JSON |
| `/jwcss` | GET | `configureCSSServlet` | Renders page-level CSS |
| `/jwscr` | GET | `configureInternalDataServlet` | Serves the site-loader JS template |

### Request Lifecycle

```
HTTP Request
 └─ Vert.x Router
     └─ Route handler
         └─ CallScope enter
             ├─ CallScopeProperties populated (RoutingContext, request, response, streamId)
             ├─ Handler logic (page render / AJAX / data / CSS / script)
             └─ CallScope exit
```

### AJAX Flow (Reactive)

```
POST /jwajax
 └─ bodyHandler (event loop)
     ├─ Deserialise AjaxCall from JSON
     ├─ Resolve event class → IEvent
     ├─ Run AjaxCallInterceptors
     └─ triggerEvent.fireEvent(call, response)   → Uni
         ├─ onItem  → write JSON response
         └─ onFailure → structured error JSON (InvalidRequestException or generic)
```

### WebSocket Broadcasting

Two `IGuicedWebSocket` implementations bridge JWebMP WebSocket messages to the Vert.x event bus:

| Class | Destination | Default |
|---|---|---|
| `VertXEventBusBridgeIWebSocket` | Direct event bus address | — |
| `VertXStompEventBusBridgeIWebSocket` | `/toStomp/<groupName>` | ✅ (bound in `JWebMPVertxBinder`) |

### Key Classes

| Class | Role |
|---|---|
| `JWebMPVertx` | Main Guice module + `VertxHttpServerConfigurator` — registers all routes and binds `@PageConfiguration` pages |
| `JWebMPVertxBinder` | Secondary Guice module — binds `ReadableUserAgent` provider, `IGuicedWebSocket`, and configures Jackson |
| `JWebMPWebSocket` | `IWebSocketMessageReceiver` stub (placeholder for custom message handling) |
| `ReadableUserAgentProvider` | Call-scoped provider that parses `User-Agent` from `HttpServerRequest` or `AjaxCall` headers |
| `VertXEventBusBridgeIWebSocket` | Broadcasts to raw event bus addresses |
| `VertXStompEventBusBridgeIWebSocket` | Broadcasts to STOMP-prefixed event bus addresses |

## ⚙️ Configuration

| Environment Variable | Default | Purpose |
|---|---|---|
| `BIND_JW_PAGES` | `true` | Enable/disable automatic page route registration |

Page binding can be disabled by setting `BIND_JW_PAGES=false` — useful when you want to register routes manually or in test harnesses.

## 🔗 Dependencies

```
com.jwebmp.vertx
 ├── com.jwebmp.client          (JWebMP client library)
 ├── com.jwebmp.core            (JWebMP core — pages, events, AJAX)
 ├── com.guicedee.vertx.web     (GuicedEE Vert.x HTTP/Web layer)
 ├── com.guicedee.guicedinjection (Guice DI + call scopes)
 ├── com.guicedee.jsonrepresentation (JSON/Jackson utilities)
 ├── io.vertx.core              (Vert.x runtime)
 ├── io.vertx.web               (Vert.x Web / Router)
 ├── uadetector-core/resources  (User-agent detection)
 └── org.apache.commons.lang3   (ExceptionUtils)
```

## 🛠️ Build

- **Java**: 25 LTS
- **Maven**: inherits `com.jwebmp:parent:2.0.0-RC5`
- **JPMS**: module descriptor at `src/main/java/module-info.java`

```bash
mvn clean install
```

## 🔄 CI

GitHub Actions workflow at `.github/workflows/maven-publish.yml` (GuicedEE shared workflow).

Required secrets: `USERNAME`, `USER_TOKEN`, `SONA_USERNAME`, `SONA_PASSWORD`.

## 📖 Documentation

| Document | Path |
|---|---|
| Architecture diagrams | `docs/architecture/README.md` |
| Prompt reference | `docs/PROMPT_REFERENCE.md` |
| Rules submodule | `rules/README.md` |
| GuicedEE Vert.x (consumers & publishers) | `../../GuicedEE/vertx/README.md` |

## 🤝 Contributing

Issues and pull requests are welcome. Please add tests (using `jwebmp-testlib`) for new routes, event handlers, or interceptors.

## 📄 License

[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
