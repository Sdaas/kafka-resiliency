package com.daasworld.kafkaresiliency;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.LoggingErrorHandler;

@Slf4j
public class MyLoggingErrorHandler extends LoggingErrorHandler {

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {
        log.error("Oh Crap! Got exception for {}", record.value());
    }
}
