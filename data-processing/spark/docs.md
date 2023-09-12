# Apache Spark 

## Install Apache Spark
1. Download and extract the Spark package from the Apache Spark website:

```bash
wget https://downloads.apache.org/spark/spark-3.2.0/spark-3.2.0-bin-hadoop3.2.tgz
tar xzf spark-3.2.0-bin-hadoop3.2.tgz
```
2. Set the SPARK_HOME environment variable to the location of the extracted Spark package:
```bash
export SPARK_HOME=/path/to/spark-3.2.0-bin-hadoop3.2
```
3. Add the Spark binaries to your PATH environment variable:
```bash
export PATH=$PATH:$SPARK_HOME/bin
```
4. Verify that Spark is installed correctly by running the following command:
```bash
spark-shell
```