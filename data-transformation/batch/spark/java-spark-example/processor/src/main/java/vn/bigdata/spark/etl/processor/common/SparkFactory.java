package vn.bigdata.spark.etl.processor.common;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.JavaConversions;
import vn.bigdata.spark.etl.processor.config.ConfigKey;
import vn.bigdata.spark.etl.processor.entity.ETLPipelineConfig;
import vn.bigdata.spark.etl.processor.entity.ETLSparkConfig;

import java.util.Map;

public class SparkFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkFactory.class);

    public static SparkSession createSparkSession(ETLPipelineConfig etlConf) {
        SparkSession.Builder builder = SparkSession.builder();
        SparkConf conf = new SparkConf();
        ETLSparkConfig sparkConf = etlConf.getJob().getSparkConf();
        String jobName = sparkConf.getAppName();
        String master = sparkConf.getMaster();
        Map<String, String> options = sparkConf.getConf();
        if (options == null || options.isEmpty()) {
            return builder
                    .appName("Spark ETL Tool")
                    .master("local[*]")
                    .getOrCreate();
        } else {
            String hiveSupport = options.getOrDefault(ConfigKey.ENABLE_HIVE_SUPPORT,options.get("spark.sql.warehouse.dir"));
            String checkpointDir = options.get(ConfigKey.checkpointDir);
            String cleanCheckpoints = options.get(ConfigKey.cleanCheckpoints);
            LOGGER.info("Creating `SparkSession` with options: {}", options.toString().replace("=", " -> ").replace(", ", "\n"));
            conf.setAppName(jobName)
                    .setMaster(master)
                    .setAll(JavaConversions.mapAsScalaMap(options));
        }
        SparkSession spark =  builder
                .config(conf)
                .getOrCreate();
        spark.sparkContext().setCheckpointDir("path");
        return spark;
    }
}
