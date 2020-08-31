
[![Build Status](https://api.travis-ci.com/Sdaas/karate-kafka.svg?branch=master)](https://travis-ci.com/Sdaas/karate-kafka)
 

## Introduction

Sample code to demonstrate Kafka resiliency patterns using Spring Kafka. This include

- Deserialization Error (TBD)
- Retry using Spring-Retry (TBD)
- Writing to a retry-topic (TBD)
- Writing to a dead-letter-queue (TBD)

## Installation

Must have the following on your machine

- Java 1.8.x
- Maven 3.6.3
- Docker 19.03.12
- Docker compose 1.26-2
- Kafka command-line shell 2.4.0

For the kafka command-line tools, the simplest way is to run `brew install kafka`. This will also end up installing the zookeeper and kafka broker on the local machine, but we will not use that.

## Managing the local Kafka broker

The configuration for Kafka and Zookeeper is specified in `kafka-single-broker.yml`. See
[Wurstmeister's Github wiki](https://github.com/wurstmeister/kafka-docker) on how to configure this.

### Starting up the Kafka cluster

From the command line, run 

```
$ ./setup.sh
Starting karate-kafka_zookeeper_1 ... done
Starting karate-kafka_kafka_1     ... done
CONTAINER ID        IMAGE                    NAMES
ce9b01556d15        wurstmeister/zookeeper   karate-kafka_zookeeper_1
33685067cb82        wurstmeister/kafka       karate-kafka_kafka_1
*** sleeping for 10 seconds (give time for containers to spin up)
*** the following topic were created ....
test-topic
```
### Smoke test the Kafka cluster

Start a consumer that will echo whatever the producer writes to `test-topic`

```
$ ./consumer.sh
```

In another terminal start off a producer, and enter data in `key:value` format 
```
$ ./producer.sh
```

Type something into the producer. If all goes well, you should see the consumer echo it back.

### Tearing down the Kafka cluster

From the command-line, run
```
$ ./teardown.sh
```

This stops zookeeper and the Kafka broker containers, and also deletes them. So all the data written to the kafka cluster will be lost. During testing, this is good because it allows us to start each test from the same known state.

### Error Handling in Kafka

#### Handling Serialization Errors

The basic idea is to deserialize using an `ErrorHandlingDeserializer` and have that delegate to the "real" deserializer. This can be done
either via configuration 
```
spring:
  kafka:
    consumer:
      # Configures the Spring Kafka ErrorHandlingDeserializer that delegates to the 'real' deserializers
      key-deserializer: org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
    properties:
      # Delegate deserializers
      spring.deserializer.key.delegate.class: org.apache.kafka.common.serialization.StringDeserializer
      spring.deserializer.value.delegate.class: org.apache.kafka.common.serialization.LongDeserializer
```
or explicitly as shown below :
```
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Long> consumerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ...);
        ...
        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());
        ErrorHandlingDeserializer<Long> valueDeserializer = new ErrorHandlingDeserializer<>(new LongDeserializer());
        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Long>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Long> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```
#### Handling Listener Errors

From Spring Kafka 2.5.x onwards, the `KafkaListenerContainerFactor` uses a `SeekToCurrentErrorHandler` as the default `errorHandler`.
This can be overridden by specifying the error handler as a bean:
```
@Bean
public ErrorHandler errorHandler() {
    return new MyLoggingErrorHandler();
}
```
or explicitly as below 
```
@Bean
public ConcurrentKafkaListenerContainerFactory<String, Long> kafkaListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, Long> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(consumerFactory);

    // Default SeekToCurrentErrorHandler() is the default from Kafka-Spring 2.5 onwards
    // factory.setErrorHandler( new SeekToCurrentErrorHandler());

    // The LoggingErrorHandler simply logs an error and moves on.
    //factory.setErrorHandler( new LoggingErrorHandler());

    // Specifying a SeekToCurrentHandler with backoff and retries ...
    // factory.setErrorHandler( new SeekToCurrentErrorHandler( new FixedBackOff(20000,3)));

    return factory;
}
```
### References

* [Confluent Blog Post - Error Handling in Kafka](https://www.confluent.io/blog/spring-for-apache-kafka-deep-dive-part-1-error-handling-message-conversion-transaction-support/)
* [ErrorHandlingDeserializer](https://docs.spring.io/spring-kafka/docs/2.5.5.RELEASE/reference/html/#error-handling-deserializer)
* [Running Kafka inside Docker](https://github.com/wurstmeister/kafka-docker)
* [Markdown syntax](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)


