#!/usr/bin/env bash
docker run -p 9042:9042 \
  -e CASSANDRA_CLUSTER_NAME="TestCluster" \
  -e CASSANDRA_ENDPOINT_SNITCH="SimpleSnitch" \
  -d \
  cassandra:latest