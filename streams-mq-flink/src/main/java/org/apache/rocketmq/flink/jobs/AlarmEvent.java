/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.jobs;

import java.util.Properties;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.rocketmq.flink.RocketMQConfig;
import org.apache.rocketmq.flink.RocketMQSource;
import org.apache.rocketmq.flink.common.serialization.SimpleKeyValueDeserializationSchema;

public class AlarmEvent {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment ();

        // enable checkpoint
        env.enableCheckpointing ( 3000 );

        Properties consumerProps = new Properties ();
        consumerProps.setProperty ( RocketMQConfig.NAME_SERVER_ADDR, "master:9876" );
        consumerProps.setProperty ( RocketMQConfig.CONSUMER_GROUP, "BINLOG_PRODUCER_GROUP" );
        consumerProps.setProperty ( RocketMQConfig.CONSUMER_TOPIC, "mysql-mq-flink" );

        Properties producerProps = new Properties ();
        producerProps.setProperty ( RocketMQConfig.NAME_SERVER_ADDR, "master:9876" );

        DataStreamSource aa = env.addSource ( new RocketMQSource ( new SimpleKeyValueDeserializationSchema ( "id", "body" ), consumerProps ) );


        aa.print ();
        try {
            env.execute ( "rocketmq-flink-example" );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
