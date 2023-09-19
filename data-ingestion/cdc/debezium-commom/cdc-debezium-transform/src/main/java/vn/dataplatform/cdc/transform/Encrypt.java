package vn.dataplatform.cdc.transform;

import com.ts.datalake.security.factory.EncryptorFactory;
import com.ts.datalake.security.spi.crypto.TextEncryptor;
import io.debezium.DebeziumException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.cache.Cache;
import org.apache.kafka.common.cache.LRUCache;
import org.apache.kafka.common.cache.SynchronizedCache;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.SchemaUtil;
import org.apache.kafka.connect.transforms.util.SimpleConfig;
import vn.dataplatform.cdc.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.kafka.connect.transforms.util.Requirements.requireMap;
import static org.apache.kafka.connect.transforms.util.Requirements.requireStruct;

/**
 * @author tuan.nguyen3
 */
@Slf4j
public abstract class Encrypt<R extends ConnectRecord<R>> implements Transformation<R> {

    public static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(Configuration.FIELD_FOR_ENCRYPT, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Field name for list field name need to encrypt")
            .define(Configuration.PROVIDER_FOR_ENCRYPT, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Field name for provider to get instance encrypt");
    private static final String PURPOSE = "adding crypto to record";
    private String classEncryptor;
    private String encryptedFields;
    private Cache<Schema, Schema> schemaUpdateCache;

    public Encrypt() {
        super();
    }

    @SneakyThrows
    @Override
    public R apply(R record) {
        if (operatingSchema(record) == null) {
            return applySchemaless(record);
        } else {
            return applyWithSchema(record);
        }
    }

    private TextEncryptor getEncryptor(String name) {
        try {
            return EncryptorFactory.getEncryptor(name);
        } catch (Exception ex) {
            throw new DebeziumException("Error while instantiating text encryptor '" + name + "'", ex);
        }
    }

    private R applySchemaless(R record) {
        final Map<String, Object> value = requireMap(operatingValue(record), PURPOSE);
        final Map<String, Object> updatedValue = new HashMap<>(value);
        List<String> fields = StringUtils.getFields(this.encryptedFields);
        if (fields.isEmpty()) {
            return record;
        }
        fields.forEach(field -> {
            if (value.containsKey(field)) {
                try {
                    String valueNeedEncrypt = (String) value.get(field);
                    String encrypted = this.getEncryptor(this.classEncryptor).encrypt(valueNeedEncrypt);
                    updatedValue.remove(field);
                    updatedValue.put(field, encrypted);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        return newRecord(record, null, updatedValue);
    }

    private R applyWithSchema(R record) {
        final Struct value = requireStruct(operatingValue(record), PURPOSE);
        Schema updatedSchema = schemaUpdateCache.get(value.schema());
        if (updatedSchema == null) {
            updatedSchema = makeUpdatedSchema(value.schema());
            schemaUpdateCache.put(value.schema(), updatedSchema);
        }
        final Struct updatedValue = new Struct(updatedSchema);
        for (Field field : value.schema().fields()) {
            updatedValue.put(field.name(), value.get(field));
        }
        List<String> fields = StringUtils.getFields(this.encryptedFields);
        if (fields.isEmpty()) {
            return record;
        }
        for (Field field : value.schema().fields()) {
            String fieldName = field.name();
            if (fields.contains(fieldName)) {
                try {
                    String valueNeedEncrypt = (String) value.get(fieldName);
                    String encrypted = this.getEncryptor(this.classEncryptor).encrypt(valueNeedEncrypt);
                    updatedValue.put(field.name(), encrypted);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return newRecord(record, updatedSchema, updatedValue);
    }

    private Schema makeUpdatedSchema(Schema schema) {
        final SchemaBuilder builder = SchemaUtil.copySchemaBasics(schema, SchemaBuilder.struct());
        for (Field field : schema.fields()) {
            builder.field(field.name(), field.schema());
        }
        return builder.build();
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void configure(Map<String, ?> configs) {
        SimpleConfig config = new SimpleConfig(CONFIG_DEF, configs);
        String encryptedFields = config.getString(Configuration.FIELD_FOR_ENCRYPT);
        String classEncryptor = config.getString(Configuration.PROVIDER_FOR_ENCRYPT);
        if (encryptedFields != null && !encryptedFields.trim().isEmpty()) {
            this.encryptedFields = encryptedFields.trim();
        }
        if (classEncryptor != null && !classEncryptor.trim().isEmpty()) {
            this.classEncryptor = classEncryptor.trim();
        }
        log.info("transformation with class crypto provider name is {} and encrypted fields are {}", this.classEncryptor, this.encryptedFields);
        schemaUpdateCache = new SynchronizedCache<>(new LRUCache<>(16));
    }

    @Override
    public void close() {
        // No resources to clean up
        schemaUpdateCache = null;
    }


    protected abstract Schema operatingSchema(R record);

    protected abstract Object operatingValue(R record);

    protected abstract R newRecord(R record, Schema updatedSchema, Object updatedValue);

    public static class Key<R extends ConnectRecord<R>> extends Encrypt<R> {
        @Override
        protected Schema operatingSchema(R record) {
            return record.keySchema();
        }

        @Override
        protected Object operatingValue(R record) {
            return record.key();
        }

        @Override
        protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
            return record.newRecord(record.topic(), record.kafkaPartition(), updatedSchema, updatedValue, record.valueSchema(), record.value(), record.timestamp());
        }


    }

    public static class Value<R extends ConnectRecord<R>> extends Encrypt<R> {
        @Override
        protected Schema operatingSchema(R record) {
            return record.valueSchema();
        }

        @Override
        protected Object operatingValue(R record) {
            return record.value();
        }

        @Override
        protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
            return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(), updatedSchema, updatedValue, record.timestamp());
        }


    }
}
