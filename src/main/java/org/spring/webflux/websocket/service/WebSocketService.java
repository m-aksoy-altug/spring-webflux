package org.spring.webflux.websocket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class WebSocketService {
	private static final Logger log= LoggerFactory.getLogger(WebSocketService.class);
	
	public Mono<String> processMessage(String message) {
        return Mono.just("Web Socker with Business logic: " + new StringBuilder(message).toString());
    }
	
}
