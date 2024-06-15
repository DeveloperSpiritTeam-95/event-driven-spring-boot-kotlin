package com.start.eventdrivenspringbootkotlin;

import com.mongodb.client.MongoClient;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EventDrivenSpringBootKotlinApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventDrivenSpringBootKotlinApplication.class, args);
        System.out.println("Application Running Successfully...@");
    }

    @Bean
    public TokenStore tokenStore(MongoClient client) {
        return MongoTokenStore.builder().mongoTemplate(DefaultMongoTemplate.builder().
                mongoDatabase(client).build()).serializer(JacksonSerializer.defaultSerializer()).build();
    }

    @Bean
    public EventStore eventStore(EventStorageEngine storageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine).build();
    }

    @Bean
    public EventStorageEngine eventStorageEngine(MongoClient client) {
        return MongoEventStorageEngine.builder()
                .eventSerializer(JacksonSerializer
                        .defaultSerializer())
                .snapshotSerializer(JacksonSerializer.defaultSerializer())
                .mongoTemplate(DefaultMongoTemplate.builder()
                        .mongoDatabase(client).build()).build();
    }


}
