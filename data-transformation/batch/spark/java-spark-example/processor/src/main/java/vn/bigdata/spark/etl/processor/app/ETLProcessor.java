package vn.bigdata.spark.etl.processor.app;

import com.spark.etl.core.util.EnvUtil;
import com.spark.etl.core.util.StringUtil;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.bigdata.spark.etl.processor.common.SparkFactory;
import vn.bigdata.spark.etl.processor.entity.ETLPipelineConfig;

import java.io.IOException;

public class ETLProcessor extends ETLProcessAbstract {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLProcessor.class);
    public static void main(String[] args) throws IOException {
        String fileConf = EnvUtil.getEnv("ETL_CONF_FILE", "/Users/tuan.nguyen3/Documents/Personal-Projects/spark-etl-tool/integration/conf/test_etl.yaml");
        LOGGER.info("Starting ETL Processor with file conf {}", fileConf);
        if(StringUtil.isEmpty(fileConf)) {
            LOGGER.error("ETL_CONF_FILE is not set");
            System.exit(1);
            return;
        }
        ETLPipelineConfig etlPipelineConfig = new ETLPipelineConfig().loadData(fileConf);
        LOGGER.info("ETL Pipeline Config: {}", etlPipelineConfig.getVersion());
        SparkSession spark = SparkFactory.createSparkSession(etlPipelineConfig);
        run(spark, etlPipelineConfig);
    }
}

