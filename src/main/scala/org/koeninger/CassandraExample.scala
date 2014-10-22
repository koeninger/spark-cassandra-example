package org.koeninger

import org.apache.spark._
import com.datastax.spark.connector._

object CassandraExample extends App {
  // only setting app name, all other properties can be specified at runtime
  val conf = new SparkConf(true).setAppName("cassandra-example")

  val sc = new SparkContext(conf)

  // assumes test.basic table already exists and has data
  val basic = sc.cassandraTable("test", "basic")
  println(basic.first)

  sc.stop
}
