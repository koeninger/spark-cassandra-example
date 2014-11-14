package org.koeninger

/** Per-key counts. Assumes underlying iterator contains keys already grouped in order */
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
