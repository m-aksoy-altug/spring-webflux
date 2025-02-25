package org.spring.webflux.router;


import org.spring.webflux.handler.RequestHandlerAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * Uses functional routing (RouterFunctions), not annotations.
 * More functional programming style, which can be useful for advanced WebFlux use cases.
*/
@Configuration
public class RouterConfig {
	
	@Autowired 
	RequestHandlerAsService requestHandlerAsService;
	
	@Bean
	public RouterFunction<ServerResponse> routerFunction(){
		return RouterFunctions.route()
				.GET("/api",request -> requestHandlerAsService.fetchAll(request))
				.GET("/api/{id}",request -> requestHandlerAsService.fetchbyId(request))
				.build();
	}

}
