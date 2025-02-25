package org.spring.webflux.repo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.webflux.entity.DummyEntity;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DummyRepository {
	private static final Logger log= LoggerFactory.getLogger(DummyRepository.class);
	
	private long minDay = LocalDate.of(2025,Month.JANUARY,1).toEpochDay();
	private long maxDay = LocalDate.of(2025,Month.DECEMBER,30).toEpochDay();
	
	public Flux<DummyEntity> fecthAll(){
		return Flux.range(1,10)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i-> log.info("Processing count: "+i))
				.map(i-> new DummyEntity(i, 
						Thread.currentThread().threadId()+":"+Thread.currentThread().getName()
						,LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay,maxDay)),
						ThreadLocalRandom.current().nextInt(234,4534)));
	}
	
	public Mono<DummyEntity> fecthById(int id){
		return Flux.range(1,10)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i-> log.info("Processing count: "+i))
				.map(i-> new DummyEntity(i, 
						Thread.currentThread().threadId()+":"+Thread.currentThread().getName(),
						LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay,maxDay)),
						ThreadLocalRandom.current().nextInt(234,4534)))
				.filter(i-> i.getId()==id).next();
	}
	
	public List<DummyEntity> fetchAllBlocking(){
		return IntStream.rangeClosed(1, 10)
				.peek(x -> {  // only for testing 
					try {
						Thread.sleep(1_000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				})
				.mapToObj(i-> new DummyEntity(i, 
						Thread.currentThread().threadId()+":"+Thread.currentThread().getName()
						,LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay,maxDay)),
						ThreadLocalRandom.current().nextInt(234,4534)))
				.collect(Collectors.toList());
	}
	
	public DummyEntity fetchbyIdBlocking(int id){
		return IntStream.rangeClosed(1, 10)
				.peek(x -> {  // only for testing 
					try {
						Thread.sleep(1_000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				})
				.mapToObj(i-> new DummyEntity(i, 
						Thread.currentThread().threadId()+":"+Thread.currentThread().getName()
						,LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay,maxDay)),
						ThreadLocalRandom.current().nextInt(234,4534)))
				.filter(i -> i.getId() ==id).findFirst().get();
	}
	
}
