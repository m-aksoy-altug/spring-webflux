package org.spring.webflux.config;

import org.spring.webflux.service.RequestHandlerAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

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
