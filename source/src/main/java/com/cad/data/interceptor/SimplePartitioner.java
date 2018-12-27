package com.cad.data.interceptor;


import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class SimplePartitioner implements Partitioner {

    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        String k = (String) key;
        return  Math.abs ( k.hashCode () ) % numPartitions;
    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }
}