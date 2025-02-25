package org.spring.webflux.service;

import java.util.List;
import java.util.stream.Collectors;

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
public class StreamRestService {
	
	private static final Logger log= LoggerFactory.getLogger(StreamRestService.class);
	
	@Autowired
	DummyRepository dummyRepository;
	
	public Flux<DummyDTO> fetchAll(){
		long start= System.currentTimeMillis();
		Flux<DummyEntity>  dummyfluxAll= dummyRepository.fecthAll();
		Flux<DummyDTO> dataTransferObject = dummyfluxAll.map(x ->
											DummyDTO.entityToDto(x));		
		log.info("Stream Rest fecthAll total execution time:"+ (System.currentTimeMillis()-start));
		return dataTransferObject;
	} 
	
	public Mono<DummyDTO> fetchbyId(int id){
		long start= System.currentTimeMillis();
		Mono<DummyEntity>  dummyfluxById= dummyRepository.fecthById(id);
		Mono<DummyDTO> dataTransferObject = dummyfluxById.map(x ->
											DummyDTO.entityToDto(x));
		log.info("fecthById total execution time:"+ (System.currentTimeMillis()-start));
		return dataTransferObject;
	}
	
	public List<DummyDTO> fetchAllBlocking(){
		long start= System.currentTimeMillis();
		List<DummyEntity>  dummy= dummyRepository.fetchAllBlocking();
		List<DummyDTO> dummyDto = dummy.stream().map
				(x->  DummyDTO.entityToDto(x)).collect(Collectors.toList());
		log.info("Stream Rest fecthAll blocking total execution time:"+ (System.currentTimeMillis()-start));
		return dummyDto;
	} 
	
	public DummyDTO fetchbyIdBlocking(int id){
		long start= System.currentTimeMillis();
		DummyEntity  dummyfluxById= dummyRepository.fetchbyIdBlocking(id);
		DummyDTO dataTransferObject = DummyDTO.entityToDto(dummyfluxById);
		log.info("fecthById total execution time:"+ (System.currentTimeMillis()-start));
		return dataTransferObject;
	}
}
