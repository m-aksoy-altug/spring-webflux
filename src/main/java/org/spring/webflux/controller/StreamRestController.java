package org.spring.webflux.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.spring.webflux.dto.DummyDTO;
import org.spring.webflux.entity.DummyMongo;
import org.spring.webflux.exception.CustomException;
import org.spring.webflux.service.StreamRestService;

/*
 * Spring WebFlux - Annotated controllers -EX: @GetMapping
 * - Built upon the existing MVC controller methods with a few changes
 * Easier to implement, especially for CRUD operations.
*/
@RestController // indicates that StreamRestController handles web request
@RequestMapping("/stream/rest")
public class StreamRestController {
	
	private static final Logger log= LoggerFactory.getLogger(StreamRestController.class);
	
	@Autowired
	StreamRestService streamRestService;
	
	@Autowired
	WebClient webClient;
	
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
	// curl http://192.168.1.113:8080/stream/rest/non-blocking-e2e
	@GetMapping(value="/non-blocking-e2e",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<DummyDTO> fetchAlFromMongo(){
		return streamRestService.fetchAllFromMongo();
	}

	/*
	 * curl -X POST "http://192.168.1.113:8080/stream/rest/non-blocking-add" \
     -H "Content-Type: application/json" \
     -d '{"id": 1,"name": "Item D","highOn": "2025-02-25","price": 17458}'
	*/
	@PostMapping(value="/non-blocking-add",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<DummyMongo> addDummyToMongo(@RequestBody DummyDTO dummyDTO){
		return streamRestService.addToMongo(dummyDTO);
	}
	/*
	 * Server-Side-Events (SSE)
	 * text/event-stream convert response to Asynchronous, non-Blocking data, 
	 * Response be pushed as a stream from the server, this also means the changes
	 * in the data repository are automatically updated to the client.
	 * SSE is to provide one-way server-to-client communications.
	 * SSE is an HTTP standard that helps in reciveing server side signal/data
	 * 
	*/
	@GetMapping(value="/non-blocking/{id}",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<DummyDTO> fetchbyId(@PathVariable("id") int id){
		Mono<DummyDTO> result= streamRestService.fetchbyId(id);
		return result;
	}
	
	@GetMapping(value="/non-blocking-error",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<DummyDTO> testNonBlocking(){
		throw new CustomException("Throwing custom exception....");
	}
	
	/*
	 * webClient.get().uri(path).retrieve() allow to access background processes
	 * even the new request is sent to the server.
	*/
	@GetMapping(value="/fetch-external-api/{path}",produces =MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<DummyDTO> fetchExternal(@PathVariable("path") String path){
		return webClient.get().uri(path).retrieve()
				.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus)
						,clientResponse-> Mono.empty())
				.bodyToFlux(DummyDTO.class);
//				.onErrorResume(WebClientRequestException.class, ex -> {
//	                log.error("External API unreachable: {}", ex.getMessage());
//	                return Flux.empty(); 
//	            });
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
		return result;
	}
	
	@GetMapping(value="/blocking-error")
	public DummyDTO testBlocking(){
		throw new CustomException("Throwing custom exception....");
	}
}
