package com.cad.data.stream

import java.util.Properties

import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object AlarmEvent {

  def main(args: Array[String]): Unit = {

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "master:9092")
    properties.setProperty("zookeeper.connect", "master:2181")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()

    val windowCounts: DataStreamSource[String] = env
      .addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))

    //      .flatMap { w => w.split("\\s") }
    //      .map { w => WordWithCount(w, 1) }
    //      .keyBy("word")
    //      .timeWindow(Time.seconds(2))
    //      .sum("count")

    windowCounts.print()


    env.execute("AlarmEvent")

  }


}