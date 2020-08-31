package com.daasworld.kafkaresiliency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyKafkaListener {

    //TODO : Read Topic from configuration file

    private MessageProcessor mp = new MessageProcessor();

    @KafkaListener(topics = "test-topic")
    public void listen(@Payload long value,
        @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Reading topic={} partition={} offset={}, key={}, value={}", topic, partition, offset, key, value );
        mp.process(value);
    }


}
