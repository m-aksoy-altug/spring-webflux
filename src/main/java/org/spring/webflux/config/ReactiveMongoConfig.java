package org.spring.webflux.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class ReactiveMongoConfig {
	
	private static final Logger log= LoggerFactory.getLogger(ReactiveMongoConfig.class);
	
	@Value("${spring.data.mongodb.port}")
    private Integer port;
	
	@Value("${spring.data.mongodb.username}")
    private String userName;
	
	@Value("${spring.data.mongodb.userpassword}")
    private String userPassword;
	
	@Value("${spring.data.mongodb.url}")
    private String url;
	
	@Value("${spring.data.mongodb.database}")
    private String databaseName;
    
	@Value("${spring.data.mongodb.authsource}")
    private String authSource;
		
	@Bean
    public MongoClient reactiveMongoClient() {
		StringBuilder connectionStr =new StringBuilder();
		connectionStr
		.append("mongodb://")
		.append(userName).append(":").append(userPassword)
		.append("@").append(url).append(":").append(port)
		.append("/").append(databaseName)
		.append("?authSource=").append(authSource);
		log.info("Connection str: "+ connectionStr.toString());
		// mongodb://admin:adminpassword@localhost:27017/reactive?authSource=admin
        return MongoClients.create(connectionStr.toString());
    }
	
}
