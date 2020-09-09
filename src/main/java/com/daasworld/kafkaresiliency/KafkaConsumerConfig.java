package com.daasworld.kafkaresiliency;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.function.BiFunction;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // TODO this template is for the DLT
    @Autowired
    private KafkaTemplate<String, Long> template;

    @Autowired
    private ConsumerFactory<String, Long> consumerFactory;

    @Bean
    public ErrorHandler errorHandler() {
        // https://docs.spring.io/spring-kafka/docs/current/api/org/springframework/kafka/listener/DeadLetterPublishingRecoverer.html
        // DLT processor publishes to topic topic.DLT

        //TODO Fix the hard-coded casting to KafkaOperations
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer((KafkaOperations) template, (rec,ex) -> new TopicPartition("dlq-topic",0));
        return new SeekToCurrentErrorHandler(recoverer, new FixedBackOff(5000,3));
    }


}