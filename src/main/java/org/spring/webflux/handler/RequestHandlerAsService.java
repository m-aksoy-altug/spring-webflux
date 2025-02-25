package org.spring.webflux.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.webflux.dto.DummyDTO;
import org.spring.webflux.entity.DummyEntity;
import org.spring.webflux.repo.DummyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandlerAsService {
	
	private static final Logger log= LoggerFactory.getLogger(RequestHandlerAsService.class);
	
	@Autowired
	DummyRepository dummyRepository;
	
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
	
	
}
