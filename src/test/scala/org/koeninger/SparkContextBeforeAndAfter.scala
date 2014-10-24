package org.koeninger

import org.apache.spark._
import org.scalatest.{BeforeAndAfter, Suite}
import org.apache.log4j.{Logger, Level}

/** Create a spark context before each test, stop it afterwards.  See
 http://spark-summit.org/wp-content/uploads/2014/06/Testing-Spark-Best-Practices-Anupama-Shetty-Neil-Marshall.pdf
  */
trait SparkContextBeforeAndAfter extends BeforeAndAfter { this: Suite =>
  var sc: SparkContext = null

  /** reduce logging so test results are visible */
  def shutUpLogs(): Unit = {
    Seq("org.apache.spark", "org.eclipse.jetty").foreach { c =>
      Logger.getLogger(c).setLevel(Level.WARN)
    }
    System.setProperty("akka.stdout-loglevel", "WARNING")
    System.setProperty("akka.loglevel", "WARNING")
  }

  before {
    System.clearProperty("spark.driver.port")
    System.clearProperty("spark.hostPort")

    // not messing around with adding jars b/c none of the tests need our code on worker classpath
    sc = new SparkContext("local", "test")
  }

  after {
    sc.stop
    sc = null

    System.clearProperty("spark.driver.port")
    System.clearProperty("spark.hostPort")
  }
}
