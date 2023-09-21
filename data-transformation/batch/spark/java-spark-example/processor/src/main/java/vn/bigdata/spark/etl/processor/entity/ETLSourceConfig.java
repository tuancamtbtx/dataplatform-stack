package vn.bigdata.spark.etl.processor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class ETLSourceConfig implements Serializable {
    @JsonProperty
    String name;
    @JsonProperty
    String format;
    @JsonProperty
    String path;
    @JsonProperty
    String schema;

    @Override
    public String toString() {
        return "ETLSourceConfig{" +
                "format='" + format + '\'' +
                ", path='" + path + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
