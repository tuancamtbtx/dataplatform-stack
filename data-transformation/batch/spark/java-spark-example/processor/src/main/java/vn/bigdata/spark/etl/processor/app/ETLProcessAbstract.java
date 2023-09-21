package vn.bigdata.spark.etl.processor.app;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.Map;
import vn.bigdata.spark.etl.processor.entity.ETLJobConfig;
import vn.bigdata.spark.etl.processor.entity.ETLPipelineConfig;
import vn.bigdata.spark.etl.processor.sql.SparkPlaningContext;

public abstract class ETLProcessAbstract {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLProcessAbstract.class);
    /**
     * Basic pipeline: source ~> transform ~> sink
     * with multiple sources/transforms/sinks supports
     */
    public static void run(SparkSession sparkSession, ETLPipelineConfig etlConfig) {
        Map<String, String> conf = sparkSession.conf().getAll();
        LOGGER.info("Spark Configuration: {} ",conf);
        ETLJobConfig jobConf = etlConfig.getJob();
        SparkPlaningContext context = new SparkPlaningContext(sparkSession, jobConf);
        Dataset<Row> sourceDs = context.loadDataSource();
        sourceDs.show();
        sparkSession.close();
    }
}
