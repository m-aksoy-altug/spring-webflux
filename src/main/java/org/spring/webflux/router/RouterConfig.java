package org.spring.webflux.router;


import org.spring.webflux.exception.GlobalErrorHandlerFunctionalWay;
import org.spring.webflux.handler.RequestHandlerAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/*Non-Blocking (Asynchronous): The request does not wait for the response. 
 * Instead, it is reactively processed when data is available, 
 * allowing the thread to handle other tasks in the meantime.
 * - Requests are handled asynchronously, without blocking the worker thread.
 * - Uses Netty event-loop model, which allows handling thousands of concurrent 
 * requests efficiently.
*/
/*
 * Uses functional Endpoints ( Involves Routers and Handlers) 
 * More functional programming style, which can be useful for advanced WebFlux use cases.
*/
@Configuration
public class RouterConfig {
	/* - @Configuration : As a source of bean definition in the application context (Spring core)
	 * - @Bean : Object of the Router function needs to be available to server the requests.
	 * - RouterFunction : Evaluates a request predicate and routes it to a handler if not match return empty result
	 * - RouterFunction.route() : Evaluates a request predicated Get mapping and routes it to the Handler to fetch the data.
	 * - ServerRequest request : Represents a server-side HTTP request as handled by a HandlerFunction
	 * - ServerResponse response: A typed servder-side HTTP response, as returned by a handler function or filter function
	*/ 
	@Autowired 
	RequestHandlerAsService requestHandlerAsService;
	
	/*
	 * Non-blocking, reactive (functional alternative to @RestController)
	 * Defines and handles incoming HTTP requests (like a traditional REST controller)
	 * Used for handling HTTP requests within a WebFlux application
	 * 	Server-side routing (similar to @RestController)
	 * Returns: Mono<ServerResponse> or Flux<ServerResponse>
	*/
	@Bean
	public RouterFunction<ServerResponse> routerFunction(){
		return RouterFunctions.route()
				.GET("/api",request -> requestHandlerAsService.fetchAll(request))
				.GET("/api/{id}",request -> requestHandlerAsService.fetchbyId(request))
				.GET("/error",request -> requestHandlerAsService.errorTest(request))
				.GET("/external-api",request -> requestHandlerAsService.fetchExternal(request,"/dummyPath"))
				.build();
	}
	
	
}
