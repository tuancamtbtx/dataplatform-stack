package vn.dataplatform.cdc.converter;

import io.debezium.spi.converter.ConvertedField;

import java.util.Properties;

/**
 * @author tuan.nguyen3
 */
public interface CustomConverter <S, F extends ConvertedField> {
    interface Converter {
        Object convert(Object input);
    }

    public interface ConverterRegistration<S> {
        void register(S fieldName, Converter converter);
    }

    void configure(Properties props);
    void converterFor(F field, ConverterRegistration<S> registration);
}
