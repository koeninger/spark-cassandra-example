package org.koeninger

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._

/** Because the cassandra driver will return all cells for a given rowkey in the same partition,
    aggregating using mapPartitions can be more efficient */
object PartitionGroupingExample {
  /** Per-key counts. Assumes underlying iterator contains keys already grouped */
  class CountingIterator[A <: AnyRef](it: Iterator[A]) extends Iterator[(A, Long)] {
    var key: A = null.asInstanceOf[A]
    var count: Long = 0

    override def hasNext: Boolean = it.hasNext || key != null

    override def next: (A, Long) = {
      while (it.hasNext) {
        val nextKey = it.next
        if (nextKey == key) {
          count += 1
        } else {
          val result = (key, count)
          key = nextKey
          count = 1
          if (result._1 != null) return result
        }
      }
      val result = (key, count)
      key = null.asInstanceOf[A]
      result
    }
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("cassandra-example-partition-grouping")

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
