package org.spring.webflux.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.webflux.websocket.service.WebSocketService;
import reactor.core.publisher.Mono;

/*
 * Reactive Web Socket for /ws
*/

@Component
public class ReactiveWebSocket implements WebSocketHandler {
	
	private static final Logger log= LoggerFactory.getLogger(ReactiveWebSocket.class);
	
	
	@Autowired
	private WebSocketService webSocketService;
	
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		log.info("Session details"+session.toString());
		return session.send(
	            session.receive()
	            .flatMap(msg -> webSocketService.processMessage(msg.getPayloadAsText()))
                .map(session::textMessage)
	        );
	}
}
