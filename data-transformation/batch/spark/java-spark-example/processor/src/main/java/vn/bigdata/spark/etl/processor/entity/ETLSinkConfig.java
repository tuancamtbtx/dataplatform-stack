package vn.bigdata.spark.etl.processor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ETLSinkConfig implements Serializable {
    @JsonProperty
    String connection;
    @JsonProperty
    String format;

    @Override
    public String toString() {
        return "ETLSinkConfig{" +
                "connection='" + connection + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
