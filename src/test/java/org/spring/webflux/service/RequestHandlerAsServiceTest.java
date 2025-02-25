package org.spring.webflux.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.webflux.dto.DummyDTO;
import org.spring.webflux.entity.DummyEntity;
import org.spring.webflux.handler.RequestHandlerAsService;
import org.spring.webflux.repo.DummyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class RequestHandlerAsServiceTest {

	private static final Logger log = LoggerFactory.getLogger(RequestHandlerAsService.class);

	@Mock
	DummyRepository dummyRepository;

	@Mock
	private ServerRequest serverRequest;

	@InjectMocks
	private RequestHandlerAsService requestHandlerAsService;

	@Test
	void fetchAllShouldReturnDataWhenRepositoryHasData() {
		DummyEntity entity1 = new DummyEntity(1, "test1", LocalDate.now(), 325);
		DummyEntity entity2 = new DummyEntity(2, "test2", LocalDate.now(), 257);
		when(dummyRepository.fecthAll()).thenReturn(Flux.just(entity1, entity2));
		Mono<ServerResponse> response = requestHandlerAsService.fetchAll(serverRequest);
		StepVerifier.create(response).assertNext(r -> {
			assertThat(r.statusCode().is2xxSuccessful()).isTrue();
			assertThat(r.headers().getContentType()).isEqualTo(MediaType.TEXT_EVENT_STREAM);
		}).verifyComplete();
	}
	
	@Test
	void fetchAllShouldReturnDataWhenRepositoryHasDataASecDelay() {
		DummyEntity entity1 = new DummyEntity(1, "test1", LocalDate.now(), 325);
		DummyEntity entity2 = new DummyEntity(2, "test2", LocalDate.now(), 257);
		when(dummyRepository.fecthAll()).thenReturn(Flux.just(entity1, entity2));
		Mono<ServerResponse> response = requestHandlerAsService.fetchAll(serverRequest);
		when(dummyRepository.fecthAll()).thenReturn(Flux.just(entity1, entity2).delayElements(Duration.ofSeconds(1)));
	    StepVerifier.withVirtualTime(() -> requestHandlerAsService.fetchAll(serverRequest))
	        .thenAwait(Duration.ofSeconds(1))  
	        .assertNext(serverResponse -> {
	            assertThat(serverResponse.statusCode().is2xxSuccessful()).isTrue();
	            assertThat(serverResponse.headers().getContentType()).isEqualTo(MediaType.TEXT_EVENT_STREAM);
	        })
	        .verifyComplete();
	}
}
