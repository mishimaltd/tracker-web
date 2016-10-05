package com.mishima.tracker;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class MongoConfig {

    @Autowired
    public @Bean MongoDbFactory mongoDbFactory(Environment env) throws Exception {
        MongoClientURI uri = new MongoClientURI(env.getProperty("mongo.uri"));
        return new SimpleMongoDbFactory(uri);
    }

    @Autowired
    public @Bean MongoTemplate mongoTemplate(Environment env) throws Exception {
        return new MongoTemplate(mongoDbFactory(env));
    }

}
