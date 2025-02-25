package org.spring.webflux.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.spring.webflux.dto.DummyDTO;
import org.spring.webflux.service.StreamRestService;

@RestController
@RequestMapping("/stream/rest")
public class StreamRestController {
	
	private static final Logger log= LoggerFactory.getLogger(StreamRestController.class);
	
	@Autowired
	StreamRestService streamRestService;
	
	/*Non-Blocking (Asynchronous): The request does not wait for the response. 
	 * Instead, it is reactively processed when data is available, 
	 * allowing the thread to handle other tasks in the meantime.
	 * - Requests are handled asynchronously, without blocking the worker thread.
	 * - Uses Netty event-loop model, which allows handling thousands of concurrent 
	 * requests efficiently.
	*/
	@GetMapping(value="/non-blocking",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<DummyDTO> fetchAll(){
		return streamRestService.fetchAll();
	}
	
	@GetMapping(value="/non-blocking/{id}",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<DummyDTO> fetchbyId(@PathVariable("id") int id){
		Mono<DummyDTO> result= streamRestService.fetchbyId(id);
		if(result==null) {
			throw new RuntimeException("Stream Rest, Result is not found with "+ id);	
		}
		return result;
	}
	
	/*
	 * - Blocking (Synchronous): Each request waits (blocks) for the response 
	 * before continuing. 
	 * - This can lead to thread starvation when handling many concurrent requests.
	 * - Each request is processed sequentially, blocking the thread until the 
	 * database responds.
	 * */

	@GetMapping(value="/blocking" ,produces = "application/json")
	public ResponseEntity<Object> fetchAllBlocking() throws JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		List<DummyDTO> result = streamRestService.fetchAllBlocking();
		String jsonResponse = objectMapper.writeValueAsString(result);
		return ResponseEntity.ok(jsonResponse);
	}
	
	@GetMapping(value="/blocking/{id}")
	public DummyDTO fetchbyIdBlocking(@PathVariable("id") int id){
		DummyDTO result= streamRestService.fetchbyIdBlocking(id);
		if(result==null) {
			throw new RuntimeException("Rest, Result is not found with "+ id);	
		}
		return result;
	}
}
