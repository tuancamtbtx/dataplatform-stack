package vn.dataplatform.cdc.server;

import io.debezium.server.DebeziumServer;
import jakarta.inject.Inject;
//import vn.dataplatform.cdc.transform.EncryptTransform.KeyEncryptTransform;

/**
 * @author tuan.nguyen3
 */
public class MainApp {
    @Inject
    DebeziumServer debeziumServer;
}
