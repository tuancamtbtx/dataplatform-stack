from pyspark import SparkContext
from pyspark.sql import SQLContext
import time
import sys
import argparse
from pyspark.sql import SparkSession


def use_project_source_code():
    import foo
    some_int = foo.get_some_int()
    print('using project source code - calling foo.get_some_int():', some_int)

def use_sql_context(sql_context):
    df = sql_context.createDataFrame([('Alice', 1), ('Bob', 2)], ['name', 'age'])
    print('use_sql_context - dataframe rows count:', df.count())

def main(sql_context, some_arg):
    print('reading argument - some_arg value:', some_arg)
    use_project_source_code()
    use_pip_modules()
    use_sql_context(sql_context)

if __name__ == '__main__':
    # reading arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('--pip_modules', required=True, help='pip modules zip path')
    parser.add_argument('--src', required=True, help='source files zip path')
    parser.add_argument('--some_arg', type=str, required=False, default=None, help="some argument")
    args = parser.parse_args()

    # creating spark context with source project files and external pip modules
    print('creating spark context. libs:', args.pip_modules, args.src)
    # sc = SparkContext(appName="Recommendation", pyFiles=[args.pip_modules, args.src])
    # sc.setLogLevel('WARN')
    # sql_context = SQLContext(sc)
    # print('sql context created')
    spark = SparkSession.builder.appName('tuancam').getOrCreate()

    # run jobs
    startTime = time.time()
    df_load = spark.read.format("com.databricks.spark.csv").option("header", "true").load("hdfs://localhost:9000/user/VanTuan/data/2010-12-01.csv")
    print(df_load.take(5))
    # main(sql_context, args.some_arg)
    endTime = time.time()
    print('jobs total runtime: ', str(int(endTime-startTime)) + 's')
    spark.stop()
