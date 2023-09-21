package vn.bigdata.spark.etl.processor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Data
@Getter
@Setter
public class ETLSparkConfig implements Serializable {
    @JsonProperty
    String appName;
    @JsonProperty
    String master;
    @JsonProperty
    Map<String, String> conf;
    public String toString() {
        return "ETLSparkConfig{" +
                "appName='" + appName + '\'' +
                ", master='" + master + '\'' +
                "conf='" + conf + '\'' +
                '}';
    }
}
