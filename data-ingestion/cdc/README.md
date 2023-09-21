# CDC (Change Data Capture)
Change data capture (CDC) refers to the tracking of all changes in a data source (databases, data warehouses, etc.) so they can be captured in destination systems. In short, CDC allows organizations to achieve data integrity and consistency across all systems and deployment environments.

## Debezium

Debezium is an open source distributed platform for change data capture. Start it up, point it at your databases, and your apps can start responding to all of the inserts, updates, and deletes that other apps commit to your databases. Debezium is durable and fast, so your apps can respond quickly and never miss an event, even when things go wrong.


We can sync data from external source database as Mysql, Postgres, Mongo into External Sink Database other.
### Basic architecture
Debezium is a change data capture (CDC) platform that achieves its durability, reliability, and fault tolerance qualities by reusing Kafka and Kafka Connect. Each connector deployed to the Kafka Connect distributed, scalable, fault tolerant service monitors a single upstream database server, capturing all of the changes and recording them in one or more Kafka topics (typically one topic per database table). Kafka ensures that all of these data change events are replicated and totally ordered, and allows many clients to independently consume these same data change events with little impact on the upstream system. Additionally, clients can stop consuming at any time, and when they restart they resume exactly where they left off. Each client can determine whether they want exactly-once or at-least-once delivery of all data change events, and all data change events for each database/table are delivered in the same order they occurred in the upstream database.

Applications that don't need or want this level of fault tolerance, performance, scalability, and reliability can instead use Debezium's embedded connector engine to run a connector directly within the application space. They still want the same data change events, but prefer to have the connectors send them directly to the application rather than persist them inside Kafka.


![overview](https://images.ctfassets.net/8vofjvai1hpv/1ZfXCGSQCX4bEXcMrc64Sp/9f6a2dce723e4af053955d51eab7e2b3/1-narrow.png?w=468&h=237&q=100&fm=png&bg=transparent)
