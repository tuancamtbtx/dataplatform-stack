package vn.dataplatform.cdc.converter;

import io.debezium.spi.converter.RelationalColumn;
import org.apache.kafka.connect.data.SchemaBuilder;

import java.util.Properties;

/**
 * @author tuan.nguyen3
 */
public class IsbnConverter implements CustomConverter<SchemaBuilder, RelationalColumn> {
    private SchemaBuilder schemaBuilder;
    @Override
    public void configure(Properties props) {
        schemaBuilder = SchemaBuilder.string().name(props.getProperty("schema.name"));
    }

    @Override
    public void converterFor(RelationalColumn field, ConverterRegistration<SchemaBuilder> registration) {
        if("isbn".equals(field.typeName())) {
            registration.register(schemaBuilder, x -> x.toString());
        }
    }
}
