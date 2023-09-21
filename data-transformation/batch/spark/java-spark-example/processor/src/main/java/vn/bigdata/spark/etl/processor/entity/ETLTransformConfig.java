package vn.bigdata.spark.etl.processor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
public class ETLTransformConfig implements Serializable {
    @JsonProperty
    String type;
    @JsonProperty
    String condition;
    @JsonProperty
    List<String> columns;
    @JsonProperty
    String function;
    @Override
    public String toString() {
        return "ETLTransformConfig{" +
                "type='" + type + '\'' +
                ", condition='" + condition + '\'' +
                ", column='" + columns + '\'' +
                ", function='" + function + '\'' +
                '}';
    }
}
