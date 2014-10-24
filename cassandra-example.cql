CREATE KEYSPACE IF NOT EXISTS test WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

USE test;

CREATE TABLE IF NOT EXISTS hello (
  greeting TEXT,
  name TEXT,
  PRIMARY KEY (greeting)
);

INSERT INTO hello (greeting, name) VALUES ('hello', 'world');
INSERT INTO hello (greeting, name) VALUES ('hola', 'mundo');

CREATE TABLE IF NOT EXISTS user_visits (
  user TEXT,
  utc_millis BIGINT,
  store TEXT,
  PRIMARY KEY (user, utc_millis)
) WITH CLUSTERING ORDER BY (utc_millis DESC);

CREATE TABLE IF NOT EXISTS stores (
  city TEXT,  
  store TEXT,
  manager TEXT,
  PRIMARY KEY (city, store)
);
