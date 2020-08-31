package com.daasworld.kafkaresiliency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private ConsumerFactory<String, Long> consumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Long> kafkaListenerContainerFactory() {
        log.info("kafkaListenerContainerFactory() called");
        ConcurrentKafkaListenerContainerFactory<String, Long> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // Default SeeToCurrentErrorHandler() is the default from Kafka-Spring 2.5 onwards
        // factory.setErrorHandler( new SeekToCurrentErrorHandler());

        // The LoggingErrorHandler simply logs an error and moves on.
        //factory.setErrorHandler( new LoggingErrorHandler());

        // Specifying a SeekToCurrentHandler with backoff, retries, and a receovery function ...
        // TBD
        // factory.setErrorHandler( new SeekToCurrentErrorHandler( new FixedBackOff(20000,3)));

        return factory;
    }
}