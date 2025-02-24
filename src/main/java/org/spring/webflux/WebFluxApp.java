package org.spring.webflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@SpringBootApplication
public class WebFluxApp implements CommandLineRunner{
	
	private static final Logger log= LoggerFactory.getLogger(WebFluxApp.class);
	 
	public static void main(String[] args) {
		SpringApplication.run(WebFluxApp.class, args);
	}
	
	/*
	 - FLux is a publisher that produces 0 to N values, operations that return unbounded 
	multiple elements use this type.
	- Mono s a publisher that produces 0 to 1 values, operations that return a single element
	use this type.
	*/
	@Override
	public void run(String... args) {
		SpringApplication.run(WebFluxApp.class, args);
//		CycleOfReactiveStreamNonoFlux();
	}
	
	private void CycleOfReactiveStreamNonoFlux() {
		log.info("Creating Stream of MONO\n");
		monoSuccess();
		log.info("Creating Stream of MONO Error\n");
		monoError();
		log.info("Creating Stream of FLUX\n");
		fluxSuccess();
		log.info("Creating Stream of FLUX Error\n");
		fluxError();
	}
	
	
	public void monoSuccess() {
		Mono<String> str= Mono.just("Mono Success").log();
		str.subscribe(value-> System.out.println(value));
	}
	
	public void monoError() {
		Mono<?> str= Mono.just("Mono Success, throwing runtime exception").then(
				Mono.error(new RuntimeException("MonoException occured..."))).log();
		str.subscribe(value-> System.out.println(value), 
				error -> System.out.println(error.getMessage()));
	}
	
	
	public void fluxSuccess() {
		Flux<String> str= Flux.just("Flux Success1","Flux Success2","Flux Success3","Flux Success4").log();
		str.subscribe(value-> System.out.println(value));
	}
	
	public void fluxError() {
		Flux<?> str= Flux.just("Flux Success, throwing runtime exception").concatWith(
				Flux.error(new RuntimeException("Flux Exception occured..."))).log();
		str.subscribe(value-> System.out.println(value), 
				error -> System.out.println(error.getMessage()));
	}
	
}
