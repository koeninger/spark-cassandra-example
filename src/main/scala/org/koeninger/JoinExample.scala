package org.koeninger

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._

/** Simple example of joining 2 cassandra tables.  Note that this will incur shuffles, and isn't necessarily efficient */
object JoinExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("cassandra-example-join")

    val sc = new SparkContext(conf)
    
    val visits = sc.cassandraTable[(String, String)]("test", "user_visits").
      select("store", "user")

    val stores = sc.cassandraTable[(String, String)]("test", "stores").
      select("store", "city")

    val visitsPerCity = visits.join(stores).map {
      case (store, (user, city)) => (city, 1)
    }.reduceByKey(_ + _)

    val result = visitsPerCity.collect

    sc.stop

    result.foreach(println)
  }
}
