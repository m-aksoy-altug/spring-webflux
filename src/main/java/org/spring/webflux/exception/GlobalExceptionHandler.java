package org.spring.webflux.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import reactor.core.publisher.Mono;

/*
 *  Your @ControllerAdvice-based GlobalExceptionHandler only works for annotation-based route
*/
@RestControllerAdvice
//@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<String>> handleGenericException(Exception ex) {
		log.info("GlobalExceptionHandler.handleGenericException");
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("GlobalExceptionHandler.handleGenericException: " + ex.getMessage()));
        // best practice, not pass error message directly.
    }
	
	@ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<String>> handleRunTimeException(RuntimeException ex) {
		log.info("GlobalExceptionHandler.handleRunTimeException");
		return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("GlobalExceptionHandler.handleRunTimeException: " + ex.getMessage()));
    }
	
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
    	log.info("GlobalExceptionHandler.handleResourceNotFoundException");
    	return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("GlobalExceptionHandler.ResourceNotFoundException: " + ex.getMessage()));
    }
    
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleCustomException(CustomException ex) {
    	log.info("GlobalExceptionHandler.handleCustomException");
    	return Mono.just(ResponseEntity
        		.status(HttpStatus.BAD_REQUEST)
        		.body("GlobalExceptionHandler.handleCustomException: " + ex.getMessage()));
    }
    
}