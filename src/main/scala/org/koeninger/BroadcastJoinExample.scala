package org.koeninger

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._

/** If you know one side of the join is much smaller, it's efficient to broadcast it */
object BroadcastJoinExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("cassandra-example-broadcast-join")

    val sc = new SparkContext(conf)

    val storeToCity = sc.cassandraTable[(String, String)]("test", "stores").
      select("store", "city").
      collect.
      toMap

    val cityOf = sc.broadcast(storeToCity)

    val visits = sc.cassandraTable[(String, String)]("test", "user_visits").
      select("store", "user")

    val visitsPerCity = visits.map {
      case (store, user) => (cityOf.value.apply(store), 1)
    }.reduceByKey(_ + _)

    val result = visitsPerCity.collect

    sc.stop

    result.foreach(println)
  }
}
