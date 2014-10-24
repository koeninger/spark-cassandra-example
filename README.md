Prerequisites:

jdk and sbt on machine you're building from, spark and cassandra on some machine(s)

cqlsh --file cassandra-example.cql

edit cassandra-example.conf, set master and cassandra connection info for your environment.


To build an assembly jar, aka uber jar, with all dependencies included:

sbt assembly


To run:

/your/path/to/spark/bin/spark-submit --properties-file cassandra-example.conf --class org.koeninger.CassandraExample target/scala-2.10/cassandra-example-assembly-0.1-SNAPSHOT.jar


To add jar to the spark shell classpath so you can play with cassandra:

/your/path/to/spark/bin/spark-shell --properties-file cassandra-example.conf --jars target/scala-2.10/cassandra-example-assembly-0.1-SNAPSHOT.jar


To run unit test:

sbt test


For more info:

http://spark.apache.org/docs/latest/submitting-applications.html

http://spark.apache.org/docs/latest/configuration.html
