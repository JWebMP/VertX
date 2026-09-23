import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.guicedee.vertx.web.spi.VertxHttpServerConfigurator;
import com.jwebmp.vertx.JWebMPVertx;
import com.jwebmp.vertx.implementations.JWebMPVertxBinder;

module com.jwebmp.vertx {
    requires transitive com.guicedee.vertx.web;
    requires transitive com.jwebmp.core;
    requires transitive com.guicedee.guicedinjection;

    requires static lombok;

    opens com.jwebmp.vertx.implementations to com.google.guice;
    opens com.jwebmp.vertx to com.google.guice;

    provides IGuiceModule with JWebMPVertx, JWebMPVertxBinder;
    provides VertxHttpServerConfigurator with JWebMPVertx;
}