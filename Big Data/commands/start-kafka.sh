#!/usr/bin/env bash

export KAFKA_HOME=/usr/local/share/applications/kafka_2.10-0.10.1.1

"$KAFKA_HOME"/bin/kafka-console-consumer.sh \
	--zookeeper master:2181 \
	--topic flume-data \
	--from-beginning