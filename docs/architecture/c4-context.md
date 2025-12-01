# C4 Level 1 — Context

The library bridges JWebMP web applications onto a Vert.x HTTP runtime with Guice-based configuration.

```mermaid
C4Context
    title JWebMP Vert.X Context
    Person(developer, "JWebMP Developer", "Builds JWebMP apps using Vert.x runtime")
    Person_EndUser(enduser, "Browser User", "Interacts with rendered JWebMP UI")

    System_Boundary(jwebmp_vertx, "JWebMP Vert.X Library") {
        System(webapp, "JWebMP Application", "UI components and server-side rendering")
        System(vertx_bridge, "Vert.x Bridge", "Guice modules & HTTP server configurator")
    }

    System_Ext(vertx, "Vert.x 5 HTTP/Web", "Event-loop, routing, networking")
    System_Ext(guice, "Guice/GuicedEE", "Dependency injection runtime")

    Rel(developer, webapp, "Develops/configures")
    Rel(enduser, webapp, "Uses over HTTP(S)")
    Rel(vertx_bridge, vertx, "Configures VertxHttpServerConfigurator")
    Rel(webapp, vertx_bridge, "Depends on for Vert.x hosting")
    Rel(vertx_bridge, guice, "Provides IGuiceModule bindings")
    Rel(guice, webapp, "Injects components")
    Rel(vertx, enduser, "Serves HTTP responses")
```

Evidence: dependencies in `pom.xml` (Vert.x, GuicedEE, JWebMP) and services exported in `src/main/java/module-info.java`. Unknowns: detailed handlers/routes are not present in the repository.
