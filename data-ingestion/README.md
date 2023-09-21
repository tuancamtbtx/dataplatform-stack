# DATA INGESTION

Data ingestion is the process of moving data from a source into a landing area or an object store where it can be used for ad hoc queries and analytics.  A simple data ingestion pipeline consumes data from a point of origin, cleans it up a bit, then writes it to a destination. 

## Overview Architecture

![overview](https://streamsets.b-cdn.net/wp-content/uploads/Modern_Data_Integration_Data-Engineering.png)
## How Data Ingestion Works

Data ingestion extracts data from the source where it was created or originally stored, and loads data into a destination or staging area. A simple data ingestion pipeline might apply one or more light transformations enriching or filtering data before writing it to some set of destinations, a data store or a message queue. More complex transformations such as joins, aggregates, and sorts for specific analytics, applications and reporting systems can be done with additional pipelines. 


### Data Source

- Apache Kafka
- JDBC
- CDC
- HTTP Client
- HDFS

### Data Destination
- Apache Kafka
- JDBC
- Snowflake
- S3