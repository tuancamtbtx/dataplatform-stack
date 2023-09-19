package vn.dataplatform.cdc.transform;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

/**
 * @author tuan.nguyen3
 */
@Slf4j
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class TestPerformanceTransform {
    private static Map<String, Object> props = new HashMap<>();
    @Benchmark
    public void Test_Performance_Transformer(){
        Encrypt.Value<SourceRecord> cryptoTransform = new Encrypt.Value<>();
        Map<String, Object> values = new HashMap<>();
        props.put("type", "vn.dataplatform.cdc.transform.EncryptTransformer");
        props.put("fields", "phone,code");
        props.put("provider", "com.ts.datalake.security.provider.crypto.TinkDaeadEncryptorProvider");
        values.put("name", "tuan.nguyen3");
        values.put("phone", "0387902375");
        values.put("code", "abc");
        cryptoTransform.configure(props);
        SourceRecord record = new SourceRecord(null, null, "test", 0,
                null, values);
        SourceRecord transformedRecord = cryptoTransform.apply(record);
        log.info("message: {}",transformedRecord);
    }
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
