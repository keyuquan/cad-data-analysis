package com.cad.data.stream

import java.util.Properties

import com.cad.data.stream.Bean.AlarmEventBean
import com.cad.data.stream.Operation.HBaseOperation
import com.cad.data.stream.Utils.JsonUtils
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object AlarmEvent {

  def main(args: Array[String]): Unit = {

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "master:9092")
    properties.setProperty("zookeeper.connect", "master:2181")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.enableCheckpointing(100000l)
    val ds_data: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))

    ds_data.map(row => {
      val am_bean: AlarmEventBean = JsonUtils.fromAlarmEventBeanJson(row)
      val rowKey = am_bean.getAlarmEventId.toString
      HBaseOperation.putData("alarm_event", "info", "data", rowKey, row.toString)
    })

    ds_data.print()
    env.execute("AlarmEvent")

  }


}
