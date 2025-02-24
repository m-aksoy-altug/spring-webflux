//package org.spring.webflux.exception;
//
//import java.util.Map;
//
//import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
//import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import reactor.core.publisher.Mono;
//
//@Component
//public class ExceptionHandler extends AbstractErrorWebExceptionHandler{
//
//	public ExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
//			ApplicationContext applicationContext,ServerCodecConfigurer config) {
//		super(errorAttributes, resources, applicationContext);
//		this.setMessageReaders(config.getReaders());
//		this.setMessageWriters(config.getWriters());
//	}
//
//	@Override
//	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
//		return RouterFunctions.route(RequestPredicates.all(), this::handleError);
//	}
//
//	private Mono<ServerResponse> handleError(ServerRequest request) {
//		Map<String,Object> errors = this.getErrorAttributes(request, false);
//		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
//				.body(BodyInserters.fromValue(errors));
//	}
//	
//}
