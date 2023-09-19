package vn.dataplatform.cdc.transform;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

/**
 * @author tuan.nguyen3
 */
public class Format <R extends ConnectRecord<R>> implements Transformation<R> {
    @Override
    public R apply(R r) {
        return null;
    }

    @Override
    public ConfigDef config() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
