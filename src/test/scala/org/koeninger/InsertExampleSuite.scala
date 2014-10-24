package org.koeninger

import org.apache.spark._
import org.scalatest.FunSuite

class InsertExampleSuite extends FunSuite with SparkContextBeforeAndAfter {
  shutUpLogs()

  test("randomStores") {
    val stores = InsertExample.randomStores(sc, stores = 256, cities = 2)

    assert(stores.count === 256)
    assert(stores.map(_._1).distinct.count === 2)
    assert(stores.map(_._2).distinct.count === 256)
  }

  test("randomVisits") {
    val visits = InsertExample.randomVisits(sc, users = 100, visitsPerUser = 10, stores = 2)

    assert(visits.count === 1000)
    assert(visits.map(_._1).distinct.count === 100)
  }
}
