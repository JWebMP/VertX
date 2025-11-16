# Vert.x STOMP Event Bus Bridge

## Overview

This implementation provides a STOMP-based event bus bridge for JWebMP applications. It ensures that messages are broadcast to their addresses in the Vert.x event bus with the proper STOMP destination prefix (`/toStomp/`).

## Components

### VertXStompEventBusBridgeIWebSocket

This class implements the `IGuicedWebSocket` interface and broadcasts messages to the Vert.x event bus with the STOMP destination prefix. It ensures that all messages sent through the WebSocket interface are properly routed to STOMP clients.

Key features:
- Adds the `/toStomp/` prefix to all destination addresses
- Logs all message broadcasts for debugging
- Maintains compatibility with the existing WebSocket interface

### JWebMPVertxStompBinder

This class binds the `VertXStompEventBusBridgeIWebSocket` implementation to the `IGuicedWebSocket` interface, ensuring that all WebSocket messages are properly routed through the STOMP bridge.

## Usage

The implementation is automatically registered through the module system. No additional configuration is required to use it.

When sending messages to the event bus, they will automatically be prefixed with `/toStomp/` to ensure they are properly routed to STOMP clients.

Example:
```java
// This will be sent to /toStomp/myAddress
webSocket.broadcastMessage("myAddress", jsonMessage);
```

## Testing

A simple test is provided to verify the STOMP destination format. It ensures that the implementation correctly prefixes all destination addresses with `/toStomp/`.

## Integration with Angular

The Angular client automatically connects to the STOMP server and subscribes to the appropriate addresses. The EventBusService in the Angular client is configured to use the STOMP client and handle messages appropriately.