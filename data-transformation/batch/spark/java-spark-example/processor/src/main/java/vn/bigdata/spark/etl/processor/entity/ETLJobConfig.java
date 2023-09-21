package vn.bigdata.spark.etl.processor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vn.bigdata.spark.etl.processor.anotations.FieldValidation;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
public class ETLJobConfig implements Serializable {
    @JsonProperty
    @FieldValidation(fieldName = "sparkConf", isValidate = true,isRequired = true)
    ETLSparkConfig sparkConf;
    @JsonProperty
    @FieldValidation(fieldName = "sourceConf", isValidate = true,isRequired = true)
    ETLSourceConfig sourceConf;

    @JsonProperty
    @FieldValidation(fieldName = "sinkConf", isValidate = true,isRequired = true)
    ETLSinkConfig sinkConf;
    @JsonProperty
    @FieldValidation(fieldName = "transformConf", isValidate = true,isRequired = true)
    List<ETLTransformConfig> transformConf;

    @Override
    public String toString() {
        return "ETLJobConfig{" +
                "sparkConf=" + sparkConf +
                ", sourceConf=" + sourceConf +
                ", sinkConf=" + sinkConf +
                ", transformConf=" + transformConf +
                '}';
    }
}
