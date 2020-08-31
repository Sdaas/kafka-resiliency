package com.daasworld.kafkaresiliency;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProcessor {
    private Random r = new Random(1234L);
    private final double threshold = 0.1;

    public void process(long data ) {
        log.info("attempting to process {}", data);
        if(r.nextDouble() <= threshold) {
            log.error("Unable to process {} - throwing exception", data);
            throw new ProcessingException();
        }
        log.info("successfully processed {}", data);
    }
}
