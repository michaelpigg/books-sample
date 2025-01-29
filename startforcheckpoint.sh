#!/usr/bin/env bash
java -XX:CRaCCheckpointTo=/tmp/cr -XX:CRaCEngine=warp -jar build/libs/books-sample-1.0-SNAPSHOT-all.jar
