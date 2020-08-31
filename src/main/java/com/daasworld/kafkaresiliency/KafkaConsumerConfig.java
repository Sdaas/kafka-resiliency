package com.daasworld.kafkaresiliency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.LoggingErrorHandler;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private ConsumerFactory<String, Long> consumerFactory;

    @Bean
    public ErrorHandler errorHandler() {
        log.info("errorHandler() called");
        return new MyLoggingErrorHandler();
    }
}