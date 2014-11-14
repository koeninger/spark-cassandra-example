package org.koeninger

import org.apache.spark._
import org.scalatest.FunSuite

class GroupByKeyIteratorSuite extends FunSuite {
  test("empty iterator") {
    val g = new GroupByKeyIterator(List[(String, Int)]().toIterator)
    assert(g.hasNext === false)
  }

  test("single key") {
    val g = new GroupByKeyIterator(List(
      "bob" -> 1
    ).toIterator)

    assert(g.next._2 === Array(1))
  }

  test("multiple keys") {
    val g = new GroupByKeyIterator(List(
      "bob" -> 1,
      "bob" -> 2,
      "al" -> 3,
      "al" -> 4,
      "al" -> 5
    ).toIterator)

    assert(g.next._2 === Array(1, 2))
    assert(g.next._2 === Array(3, 4, 5))
  }
}
