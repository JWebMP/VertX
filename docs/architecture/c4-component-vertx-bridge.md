# C4 Level 3 — Vert.x Bridge Components

Focus on the `com.jwebmp.vertx` module that provides Guice bindings and Vert.x HTTP configuration.

```mermaid
C4Component
    title com.jwebmp.vertx Components

    Container_Boundary(bridge, "JWebMP Vert.X Library") {
        Component(guiceModule, "JWebMPVertxBinder", "GuicedEE IGuiceModule", "Binds Vert.x/JWebMP integration components")
        Component(configurator, "JWebMPVertx", "VertxHttpServerConfigurator", "Applies Vert.x HTTP server settings for JWebMP")
    }

    Container(webapp, "JWebMP App", "JWebMP Core") 
    Container(vertx, "Vert.x HTTP Server", "Vert.x 5")
    Container(guice, "Guice Injector", "GuicedEE")

    Rel(guiceModule, guice, "Registered via provides IGuiceModule")
    Rel(configurator, vertx, "Registered via provides VertxHttpServerConfigurator")
    Rel(webapp, guiceModule, "Depends on Guice bindings")
    Rel(webapp, configurator, "Runs inside configured Vert.x HTTP server")
```

Sources: `module-info.java` exports `JWebMPVertx` and `JWebMPVertxBinder`. Implementation internals are not present in the repository; update when classes are added.
