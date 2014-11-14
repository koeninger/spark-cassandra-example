package org.koeninger

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._

/** Because the cassandra driver will return all cells for a given rowkey in the same partition,
    aggregating using mapPartitions can be more efficient */
object PartitionCountingExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("cassandra-example-partition-counting")

    val sc = new SparkContext(conf)
    
    val visits = sc.cassandraTable[(String)]("test", "user_visits").
      select("user")

    val visitsPerUser = visits.mapPartitions { userIterator =>
      new CountingIterator(userIterator)
    }

    val maxVisits = visitsPerUser.values.max

    sc.stop

    println(maxVisits)
  }
}
