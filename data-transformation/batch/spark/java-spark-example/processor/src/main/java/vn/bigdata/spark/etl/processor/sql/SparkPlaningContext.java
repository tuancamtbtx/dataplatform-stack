package vn.bigdata.spark.etl.processor.sql;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.bigdata.spark.etl.processor.entity.ETLJobConfig;
import vn.bigdata.spark.etl.processor.entity.ETLSinkConfig;
import vn.bigdata.spark.etl.processor.entity.ETLSourceConfig;
import vn.bigdata.spark.etl.processor.entity.ETLTransformConfig;

import java.util.List;

public class SparkPlaningContext {
    private static Logger LOGGER = LoggerFactory.getLogger(SparkPlaningContext.class);
    ETLJobConfig etlJobConfig;
    SparkSession spark;
    public SparkPlaningContext(SparkSession spark, ETLJobConfig etlJobConfig)    {
        this.etlJobConfig = etlJobConfig;
        this.spark = spark;
    }
    public Dataset<Row> loadDataSource() {
        ETLSourceConfig sourceConf = this.etlJobConfig.getSourceConf();
        LOGGER.info("Loading Dataset From Source Name: {}", sourceConf.getName());
        String format = sourceConf.getFormat();
        String path = sourceConf.getPath();
        return this.spark.read().format(format).option("header", "true").load(path);
    }

    public Dataset<Row> transformDataset(Dataset<Row> ds, List<ETLTransformConfig> transformConf) {
        return null;
    }
    public void sinkDataset(Dataset<Row> ds) {
        ETLSinkConfig sinkConf = this.etlJobConfig.getSinkConf();
    }

}
