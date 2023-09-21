package vn.bigdata.spark.etl.processor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.bigdata.spark.etl.processor.app.ETLProcessor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Data
@Getter
@Setter
public class ETLPipelineConfig implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLProcessor.class);
    @JsonProperty
    String version;
    @JsonProperty
    ETLJobConfig job;
    public String toString() {
        return "ETLPipelineConfig{" +
                "version='" + version + '\'' +
                ", job=" + job +
                '}';
    }
    public ETLPipelineConfig loadData(String fileConf) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind
        return mapper.readValue(new File(fileConf), ETLPipelineConfig.class);
    }
}
