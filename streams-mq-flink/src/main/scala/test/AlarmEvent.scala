package test

import java.util.Properties

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.apache.hadoop.hbase.client.HTable
import org.apache.rocketmq.flink.{RocketMQConfig, RocketMQSource}
import org.apache.rocketmq.flink.Utils.HBaseUtils
import org.apache.rocketmq.flink.common.serialization.SimpleKeyValueDeserializationSchema
import org.json.JSONObject

object AlarmEvent {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // enable checkpoint
    env.enableCheckpointing(3000)

    val consumerProps = new Properties
    consumerProps.setProperty(RocketMQConfig.NAME_SERVER_ADDR, "master:9876")
    consumerProps.setProperty(RocketMQConfig.CONSUMER_GROUP, "BINLOG_PRODUCER_GROUP")
    consumerProps.setProperty(RocketMQConfig.CONSUMER_TOPIC, "mysql-mq-flink")

    val producerProps = new Properties
    producerProps.setProperty(RocketMQConfig.NAME_SERVER_ADDR, "master:9876")

    val  aa: DataStream[String] = env.addSource(new RocketMQSource[String,String](new SimpleKeyValueDeserializationSchema("id", "body"), consumerProps))

  }


}
