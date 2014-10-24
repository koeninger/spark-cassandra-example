package org.koeninger

import org.apache.spark._
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._
import scala.util.Random

/** Insert some highly realistic sample data into cassandra.
  * Assumes test.user_visits and test.stores tables already exist, see cassandra-example.cql
  */
object InsertExample extends App {
  def randomStores(sc: SparkContext, stores: Int, cities: Int): RDD[(String, String, String)] = {
    sc.parallelize(0 until stores).map { s =>
      val city = s"city_${Random.nextInt(cities)}"

      val store = s"store_${s}"
      
      val manager = s"manager_${Math.abs(Random.nextInt)}"
      
      (city, store, manager)
    }
  }

  def randomVisits(sc: SparkContext, users: Int, stores: Int, visitsPerUser: Int): RDD[(String, Long, String)] = {
    sc.parallelize(0 until users).flatMap { u =>
      val user = s"user_${u}"
      
      (1 to visitsPerUser).map { v =>
        val utcMillis = System.currentTimeMillis - Random.nextInt
      
        val store = s"store_${Random.nextInt(stores)}"
        
        (user, utcMillis, store)
      }
    }
  }

  val conf = new SparkConf().setAppName("cassandra-example-insert")

  val sc = new SparkContext(conf)
  
  val numStores = 128

  randomStores(sc, stores = numStores, cities = 32).
    saveToCassandra("test", "stores", SomeColumns("city", "store", "manager"))

  randomVisits(sc, users = 16384, visitsPerUser = 128, stores = numStores).
    saveToCassandra("test", "user_visits", SomeColumns("user", "utc_millis", "store"))

  sc.stop
}
