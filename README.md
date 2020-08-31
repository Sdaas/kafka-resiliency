
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

### References

* [Running Kafka inside Docker](https://github.com/wurstmeister/kafka-docker)
* [Markdown syntax](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)


