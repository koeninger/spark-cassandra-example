package org.koeninger

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/** Groups values by key. Assumes underlying iterator contains keys already grouped in order */
class GroupByKeyIterator[K, V: ClassTag](it: Iterator[Product2[K,V]]) extends Iterator[(K, Array[V])] {
  var key: Option[K] = None
  val values = new ArrayBuffer[V]()

  override def hasNext: Boolean = it.hasNext || key.isDefined

  override def next: (K, Array[V]) = {
    while (it.hasNext) {
      val (nextKey, nextValue) = it.next
      if (key.map(_ == nextKey).getOrElse(false)) {
        values.append(nextValue)
      } else {
        val result = key.map(k => (k, values.toArray))
        key = Some(nextKey)
        values.clear
        values.append(nextValue)
        if (result.isDefined) return result.get
      }
    }
    val result = key.map(k => (k, values.toArray))
    key = None
    result.getOrElse(throw new java.util.NoSuchElementException("next on empty iterator"))
  }
}
