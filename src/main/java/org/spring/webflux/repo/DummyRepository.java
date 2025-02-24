package org.spring.webflux.repo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
	private String[] title= {"title1","title2","title3","title4","title5"};
	
	public Flux<DummyEntity> fecthAll(){
		return Flux.range(1,10)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i-> log.info("Processing count: "+i))
				.map(i-> new DummyEntity(i, 
						title[new Random().nextInt(title.length)],
						LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay,maxDay)),
						ThreadLocalRandom.current().nextInt(234,4534)));
	}
	
	public Mono<DummyEntity> fecthById(int id){
		return Flux.range(1,10)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i-> log.info("Processing count: "+i))
				.map(i-> new DummyEntity(i, 
						title[new Random().nextInt(title.length)],
						LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay,maxDay)),
						ThreadLocalRandom.current().nextInt(234,4534)))
				.filter(i-> i.getId()==id).next();
	}
	
	
}
