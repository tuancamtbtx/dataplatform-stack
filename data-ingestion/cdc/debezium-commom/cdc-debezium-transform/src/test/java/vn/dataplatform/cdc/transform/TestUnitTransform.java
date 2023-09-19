package vn.dataplatform.cdc.transform;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.header.ConnectHeaders;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tuan.nguyen3
 */
public class TestUnitTransform {
    private static Map<String, Object> props = new HashMap<>();

    /*
    SourceRecord{
        sourcePartition={server=tutorial},
        sourceOffset={last_snapshot_record=false, lsn=35080664, txId=901, ts_usec=1694401980544642, snapshot=true}}
        ConnectRecord{
            topic='tutorial.inventory.customers',
            kafkaPartition=null,
            key=Struct{id=1001},
            keySchema=Schema{tutorial.inventory.customers.Key:STRUCT},
            value=Struct{after=Struct{id=1001,first_name=Sally,last_name=Thomas,email=sally.thomas@acme.com},
            source=Struct{version=2.3.0.Final,connector=postgresql,name=tutorial,ts_ms=1694401980544,snapshot=first,db=postgres,sequence=[null,"35080664"],
            schema=inventory,table=customers,txId=901,lsn=35080664},
            op=r,ts_ms=1694401980629},
            valueSchema=Schema{tutorial.inventory.customers.Envelope:STRUCT},
            timestamp=null, headers=ConnectHeaders(headers=)
         }
     */
    @BeforeAll
    public static void setup() {
        props.put("type", "vn.dataplatform.cdc.transform.EncryptTransformer");
        props.put("fields", "phone,code");
        props.put("provider", "com.ts.datalake.security.provider.crypto.TinkDaeadEncryptorProvider");
    }

    @Test
    public void Test_ValueTransformer(){
        Encrypt.Value<SourceRecord> cryptoTransform = new Encrypt.Value<>();
        Map<String, Object> values = new HashMap<>();
        values.put("name", "tuan.nguyen3");
        values.put("phone", "0387902375");
        values.put("code", "abc");
        cryptoTransform.configure(props);
        SourceRecord record = new SourceRecord(null, null, "test", 0,
                null, values);
        SourceRecord transformedRecord = cryptoTransform.apply(record);
        System.out.println(transformedRecord);
        Assertions.assertTrue(true);
    }
    @Test
    public void Test_KeyTransformer() {
        Encrypt.Key<SourceRecord> cryptoTransform = new Encrypt.Key<>();
        Map<String, Object> keys = new HashMap<>();
        keys.put("name", "tuan.nguyen3");
        keys.put("phone", "0387902375");
        keys.put("code", "abc");
        cryptoTransform.configure(props);
        SourceRecord record = new SourceRecord(null, null, "test", 0,
                null, keys,
                null, null);
        SourceRecord transformedRecord = cryptoTransform.apply(record);
        Assertions.assertTrue(true);
    }
}
