package com.cad.data.stream

import java.util.Properties

import com.cad.data.stream.Utils.HBaseUtils
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.json.JSONObject

object AlarmEvent {

  def main(args: Array[String]): Unit = {

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "master:9092")
    properties.setProperty("zookeeper.connect", "master:2181")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.enableCheckpointing(60000)
    val ds_data: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))

    ds_data.map(row => {
      System.out.println(System.currentTimeMillis());
      val table_alarm_event = HBaseUtils.getAlarmEventTable()
      val jsonObject = new JSONObject(row)
      val rowKey = jsonObject.get("alarmEventId").toString

      HBaseUtils.putData(table_alarm_event, "info", "data", rowKey, row.toString)

    })

    env.execute("AlarmEvent")

  }


}
