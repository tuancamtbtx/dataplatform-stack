package vn.bigdata.spark.etl.processor.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobValidate {
    private static Logger LOGGER = LoggerFactory.getLogger(JobValidate.class);

    private String pathFileConf;

    public JobValidate(String pathFileConf) {
        this.pathFileConf = pathFileConf;
    }

    public String validate() {
        return "";
    }
}
