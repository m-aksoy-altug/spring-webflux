package org.spring.webflux.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.webflux.dto.DummyDTO;
import org.spring.webflux.entity.DummyEntity;
import org.spring.webflux.exception.CustomException;
import org.spring.webflux.repo.DummyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandlerAsService {
	
	private static final Logger log= LoggerFactory.getLogger(RequestHandlerAsService.class);
	
	@Autowired
	DummyRepository dummyRepository;
	
	@Autowired
	WebClient webClient;
	
	public Mono<ServerResponse> fetchAll(ServerRequest request){
		long start= System.currentTimeMillis();
		Flux<DummyEntity>  dummyfluxAll= dummyRepository.fecthAll();
		Flux<DummyDTO> dataTransferObject = dummyfluxAll.map(x ->
											DummyDTO.entityToDto(x));		
//		dataTransferObject.filter(x-> !x.getName().startsWith("A")).log();
		dataTransferObject.map(x-> x.getName().toUpperCase()).log();
		log.info("fecthAll total execution time:"+ (System.currentTimeMillis()-start));
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(dataTransferObject,DummyDTO.class);
	} 
	
	public Mono<ServerResponse> fetchbyId(ServerRequest request){
		long start= System.currentTimeMillis();
		int id =Integer.valueOf(request.pathVariable("id"));
		Mono<DummyEntity>  dummyfluxById= dummyRepository.fecthById(id);
		Mono<DummyDTO> dataTransferObject = dummyfluxById.map(x ->
											DummyDTO.entityToDto(x));
		log.info("fecthById total execution time:"+ (System.currentTimeMillis()-start));
		return ServerResponse.ok()
				.body(dataTransferObject,DummyDTO.class);
	}
	
	public Mono<ServerResponse> errorTest(ServerRequest request){
		throw new CustomException("Throwing custom exception....");
	}
	
	public Mono<ServerResponse> fetchExternal(ServerRequest request,String path){
		long start= System.currentTimeMillis();
		Mono<String> response = webClient.get()
			      .uri(path)
			      .retrieve()
			      .bodyToMono(String.class);
		log.info("fectExternal total execution time:"+ (System.currentTimeMillis()-start));
		 return ServerResponse.ok()
		            .contentType(MediaType.APPLICATION_JSON)
		            .body(response, String.class);
	}

}
