//package org.spring.webflux.exception;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import org.springframework.boot.web.error.ErrorAttributeOptions;
//import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.ServerRequest;
//
//@Component
//public class GlobalErrorAttributes extends DefaultErrorAttributes{
//	
//	@Override
//	public Map<String,Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options){
//			Map<String,Object> errorAttributes= new LinkedHashMap<>();
//			Throwable errors= getError(request);
//			errorAttributes.put("message", errors);
//			return errorAttributes;
//	}
//}
