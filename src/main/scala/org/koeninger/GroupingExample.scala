package org.koeninger

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._

/** Simple example of grouping on a cassandra rowkey, Note that this will incur shuffles, and isn't necessarily efficient */
object GroupingExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("cassandra-example-grouping")

    val sc = new SparkContext(conf)
    
    val visits = sc.cassandraTable[(String)]("test", "user_visits").
      select("user")

    val visitsPerUser = visits.map { user =>
      (user, 1)
    }.reduceByKey(_ + _)

    val maxVisits = visitsPerUser.values.max

    sc.stop

    println(maxVisits)
  }
}
