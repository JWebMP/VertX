package com.jwebmp.vertx.implementations;

import com.google.inject.AbstractModule;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.guicedee.client.scopes.CallScope;
import com.guicedee.client.services.websocket.IGuicedWebSocket;
import net.sf.uadetector.ReadableUserAgent;


public class JWebMPVertxBinder extends AbstractModule implements IGuiceModule<JWebMPVertxBinder>
{

    @Override
    protected void configure()
    {
        bind(ReadableUserAgent.class).toProvider(ReadableUserAgentProvider.class)
                                     .in(CallScope.class);
        //bind(IGuicedWebSocket.class).to(VertXEventBusBridgeIWebSocket.class);
        bind(IGuicedWebSocket.class).to(VertXStompEventBusBridgeIWebSocket.class);

        // Vert.x JSON is explicitly routed through the shared GuicedEE Jackson 3 mapper
        // via the io.vertx.core.spi.JsonFactory SPI (see com.guicedee.vertx), so the legacy
        // Jackson 2 DatabindCodec mapper no longer needs configuring here.
    }
}
