package com.cad.data.stream

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object SocketWindowWordCount {

  def main(args: Array[String]): Unit = {
    var hostname: String = "master"
    var port: Int = 9999

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val text: DataStream[String] = env.socketTextStream(hostname, port, '\n')

    text.print()
    val windowCounts: DataStream[WordWithCount] = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy("word")
      .timeWindow(Time.seconds(10))
      .sum("count")

    windowCounts.print().setParallelism(1)

    env.execute("Socket Window WordCount")
  }

  /** Data type for words with count */
  case class WordWithCount(word: String, count: Long)

}