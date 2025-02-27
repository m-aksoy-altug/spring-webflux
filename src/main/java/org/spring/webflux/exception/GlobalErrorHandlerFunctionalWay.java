package org.spring.webflux.exception;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/*
 *  Works only for functional WebFlux routes.
 *  Handles exceptions before they propagate to the client.
 *  Allows you to customize JSON error responses.
*/
@Component
@Order(-2) // Ensures this handler runs before Spring Boot's default error handler
public class GlobalErrorHandlerFunctionalWay implements WebExceptionHandler {
	
	private static final Logger log= LoggerFactory.getLogger(GlobalErrorHandlerFunctionalWay.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper(); 
	
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		 HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; 
	        String message = "An unexpected error occurred.";
	        if (ex instanceof ResourceNotFoundException) {
	        	log.info("GlobalErrorHandlerFunctionalWay.ResourceNotFoundException");
	            status = HttpStatus.NOT_FOUND;
	            message = "GlobalErrorHandlerFunctionalWay ResourceNotFoundException: " + ex.getMessage();
	        } else if (ex instanceof CustomException) {
	        	log.info("GlobalErrorHandlerFunctionalWay.CustomException");
	            status = HttpStatus.BAD_REQUEST;
	            message = "GlobalErrorHandlerFunctionalWay CustomException: " + ex.getMessage();
	        } else if (ex instanceof RuntimeException) {
	        	log.info("GlobalErrorHandlerFunctionalWay.RuntimeException");
	            status = HttpStatus.INTERNAL_SERVER_ERROR;
	            message = "GlobalErrorHandlerFunctionalWay RuntimeException: " + ex.getMessage();
	        }

	        log.error("Handling error: {}", message, ex);
	        exchange.getResponse().setStatusCode(status);
	        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
	        Map<String, Object> errorResponse = Map.of(
	            "error", message,
	            "status", status.value(),
	            "path", exchange.getRequest().getPath().value()
	        );
	        try {
	            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
	            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
	            return exchange.getResponse().writeWith(Mono.just(buffer));
	        } catch (Exception e) {
	            return Mono.error(e);
	        }
//           byte[] bytes = errorResponse.toString().getBytes(StandardCharsets.UTF_8);
//	        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//	        return exchange.getResponse().writeWith(Mono.just(buffer));
	}
	
}