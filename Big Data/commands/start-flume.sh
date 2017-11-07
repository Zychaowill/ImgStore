#!/usr/bin/env bash

export FLUME_HOME=/usr/local/share/applications/apache-flume-1.7.0-bin

"$FLUME_HOME"/bin/flume-ng agent \
	--conf "$FLUME_HOME"/conf \
	--conf-file "$FLUME_HOME"/conf/flume2kafka.properties \
	--name logAgent \
	--Dflume.root.logger=INFO,console &