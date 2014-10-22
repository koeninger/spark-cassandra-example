Prerequisites:

jdk and sbt on machine you're building from, spark and cassandra on some machine(s)


To build:

sbt assembly


To run:

edit cassandra-example.conf, set master and cassandra connection info for your environment.

/your/path/to/spark/bin/spark-submit --properties-file cassandra-example.conf --class org.koeninger.CassandraExample target/scala-2.10/cassandra-example-assembly-0.1-SNAPSHOT.jar


For more info:

http://spark.apache.org/docs/latest/submitting-applications.html
http://spark.apache.org/docs/latest/configuration.html