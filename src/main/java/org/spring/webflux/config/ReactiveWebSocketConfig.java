package org.spring.webflux.config;

import java.util.Map;

import org.spring.webflux.websocket.ReactiveWebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;


/*
 * Web Socket connection:
 * Communication Type: Full-Duplex stateful
 * Data Exchange: Persistent bidirectional communication using frames
 * Protocol: WebSocket ws:// or wss://
 * Scalability: Requires more resources as connection persistent
 * Use case: Real-time communication, like chat apps, notifications,live data updates
 *  
*/
@Configuration
public class ReactiveWebSocketConfig {
	
	/*
	 * ws://192.168.1.113:8080/ws
	*/
	@Bean
    public SimpleUrlHandlerMapping webSocketMapping(ReactiveWebSocket webSocketHandler) {
        return new SimpleUrlHandlerMapping(Map.of("/ws", webSocketHandler), 1);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
