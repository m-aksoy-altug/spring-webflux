package org.spring.webflux;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;



@SpringBootApplication
//public class WebFluxApp implements CommandLineRunner{
	public class WebFluxApp {	
	private static final Logger log= LoggerFactory.getLogger(WebFluxApp.class);
	 
	public static void main(String[] args) {
		CycleOfReactiveStreamNonoFlux logic= new CycleOfReactiveStreamNonoFlux();
		logic.executeLogic();
		SpringApplication.run(WebFluxApp.class, args);
	}
	
	/*
	 * - Non-blocking, reactive alternative to RestTemplate
	 * - Sends outgoing HTTP requests to other services (like a REST client)
	 * - Used to consume APIs or call external services from your application
	 * - Client-side HTTP client
	 * - Returns: Mono<T> or Flux<T> (response from external services)
	*/
	@Bean
	public WebClient initilizeWebClient() {
		return WebClient.builder()
	            .baseUrl("http://localhost:9090/api")  // Base URL for external API
	            .build();
	}

	
//	@Override
//	public void run(String... args) {
//	CycleOfReactiveStreamNonoFlux logic= new CycleOfReactiveStreamNonoFlux();
//	logic.executeLogic();
//	}
	
	
}

class CycleOfReactiveStreamNonoFlux{
	
	private static final Logger log= LoggerFactory.getLogger(CycleOfReactiveStreamNonoFlux.class);
	
	public void executeLogic() {
		log.info("Creating Stream of MONO\n");
		monoSuccess();
		log.info("Creating Stream of MONO Error\n");
		monoError();
		log.info("Creating Stream of FLUX\n");
		fluxSuccess();
		log.info("Creating Stream of FLUX Error\n");
		fluxError();
		log.info("Multiple subscriber and Merged publishers\n");	
		fluxSuccessMultipleSubscribersOrMergeTwoPublishers();
		log.info("Combining two streams\n");	
		combineMultipleStreams();
		log.info("Flux stream with 2 sec delay\n");	
		fluxDelay();
		log.info("Flux stream with filering \n");	
		fluxFilter();
		log.info("Multiple Flux streams with zip \n");	
		fluxsZip();
		log.info("Multiple Flux streams with flatmap \n");	
		fluxFlatMap();
		log.info("Flux and Mono streams with zipWith \n");	
		fluxAndMonoZipWith();
		
	}
	
	/*
	 * - onSubscribe() > this in invoked  by subscriber during the subscription to the system
	 * - request(unbounded) > when subscribe is called, backstage we are creating a Subscription
	 * This subscription request element from the stream, the method default value is unbounded
	 * which means it requests every single element available
	 * - onNext() > this is invoked by the subscriber on every individual elements
	 * - onComplete() > this is invoked by the subscriber at the end, after receiving the final element.
	 * - onError() > invokes by the subscriber if there is an exception 
	*/
	/*	.log() method is used to know life cycle of methods of the stream.	
	*/
	/*	list of operations on Mono and Flux stream:
	 * - Filter - Transforming operations - Merge - Zip - ZipWith - FlatMap - Concat = Delay 	
	*/
	private void monoSuccess() {
		Mono<String> str= Mono.just("Mono Success").log();
		Mono.delay(Duration.ofSeconds(1));
		str.subscribe(value-> System.out.println(value));
	}

		
	private void monoError() {
		Mono<?> str= Mono.just("Mono Success, throwing runtime exception").then(
				Mono.error(new RuntimeException("MonoException occured..."))).log();
		str.subscribe(value-> System.out.println(value), 
				error -> System.out.println(error.getMessage()));
	}
	
	private void fluxSuccess() {
		Flux<String> str= Flux.just("abc.com","D_0","D_1","Flux Success4").log();
		List<String> list =str.collectSortedList().block();
		list.forEach(x-> System.out.println("tr.collectSortedList().block()"+x));
		str.subscribe(value-> System.out.println(value));
	}
	
	private void fluxError() {
		Flux<?> str= Flux.just("Flux Success, throwing runtime exception").concatWith(
				Flux.error(new RuntimeException("Flux Exception occured..."))).log();
		str.subscribe(value-> System.out.println(value), 
				error -> System.out.println(error.getMessage()));
	}
	
	/* - Mono and Flux streams can have any number of Subscribers can connect to pipeline
	 * - Mono and Flux streams can merged by combining two publisher 
	*/
	private void fluxSuccessMultipleSubscribersOrMergeTwoPublishers() {
		Flux<String> str= Flux.just("Flux Success1","Flux Success2","Flux Success3","Flux Success4").log();
		str.subscribe(value-> System.out.println("App1 "+value));
		str.subscribe(value-> System.out.println("App2 "+value));
		
		Mono<String> strMonoMerge= Mono.just("Mono Success").log();
		Flux<String> strFluxMerge= Flux.just("Flux Success1","Flux Success2","Flux Success3","Flux Success4").log();
		Flux<String> merged =Flux.merge(strMonoMerge,strFluxMerge);
		merged.log().subscribe(value-> System.out.println("Merged two publishers "+value));
		// zip -joins two publishers only and produce a single result
		// wait all the origins to emit one component and integrate these components into an output value
		// zipWith -joins two publishers only and produce a single result	
		Mono<String> share1= Mono.just("Mono Success").log();
		Flux<String> share2= Flux.just("Flux Success1","Flux Success2","Flux Success3","Flux Success4").log();
		Mono<List<String>> listMono= share2.collectList();	
		Mono<Tuple2<String, List<String>>> result = Mono.zip(share1, listMono);
		result.subscribe(value-> System.out.println("Merged Mono and FLux  "+value));
	}
	
	private void combineMultipleStreams() {
		Mono<String> share1= Mono.just("Mono Success").log();
		Flux<String> share2= Flux.just("Flux Success1","Flux Success2","Flux Success3","Flux Success4").log();
		Flux<String> combineStreams = Flux.concat(share1, share2);
		combineStreams
		.log().subscribe(value-> System.out.println("Combine multiple streams"+value));
	}
	private void fluxDelay() {
		Flux<String> fluxList= Flux.just("Flux with delay1","Flux with delay2","Flux with delay3").log();
		fluxList.delayElements(Duration.ofSeconds(2)).log()
		.subscribe(value-> System.out.println("Flux with 2 secs delay: "+value));
	}
	
	private void fluxFilter() {
		Flux<Integer> fluxList= Flux.just(657,345,456,215,547).log();
		Flux<Integer> fluxfiltered= fluxList.filter(x-> x>400).log();
		fluxfiltered.subscribe(value-> System.out.println("Flux filtered: "+value));
	}
	
	private void fluxsZip() {
		Flux<Integer> fluxInt= Flux.just(657,345,456,215,547).log();
		Flux<String> fluxstr= Flux.just("Flux1","Flux2","Flux3").log();
		Flux<Tuple2<Integer,String>> zip = Flux.zip(fluxInt, fluxstr);
		zip.subscribe(value-> System.out.println("Flux zip: "+value));
	}
	
	private void fluxFlatMap() {
		Flux<Integer> fluxInt= Flux.just(657,345,456,215,547).log();
		Flux<Integer> fluxInt2= Flux.just(1657,1345,1456,1215,1547).log();
		Flux<Integer> fluxInt3= Flux.just(2657,2345,2456,2215,2547).log();
		Flux<Integer> result= Flux.just(fluxInt,fluxInt2,fluxInt3).flatMap(x -> x)
		.map(x->x);
		result.subscribe(value-> System.out.println("Flux flatMap and map: "+value));
	}
	
	private void fluxAndMonoZipWith() {
		Object x= new Dummy();
		Flux<Object> fluxObj= Flux.just((Object)new Dummy(),(Object)new Dummy()).log();
		Mono<Object> monoObj = Mono.just((Object)new Dummy1());
		Flux<Tuple2<Object, Object>>  zipped = fluxObj.zipWith(monoObj);
		final var zippedAll =fluxObj.zipWith(monoObj);
		zippedAll.log().subscribe(objt-> 
		System.out.println("ZipWith objects with hashcode"+objt.hashCode()));
	}
}

class Dummy{
	private int id;
}

class Dummy1{
	private int id;
}