package vn.dataplatform.cdc.pipeline.channel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.CommonConnectorConfig;
import io.debezium.pipeline.signal.SignalRecord;
import io.debezium.pipeline.signal.channels.SignalChannelReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tuan.nguyen3
 */
@Slf4j
public class HttpSignalChannel implements SignalChannelReader {
    public static final String CHANNEL_NAME = "http";
    private static final List<SignalRecord> SIGNALS = new ArrayList<>();
    public CommonConnectorConfig connectorConfig;

    @Override
    public String name() {
        return CHANNEL_NAME;
    }

    @Override
    public void init(CommonConnectorConfig commonConnectorConfig) {
        this.connectorConfig = commonConnectorConfig;
    }

    @Override
    public <T> void reset(T reference) {
        SignalChannelReader.super.reset(reference);
    }

    @Override
    public List<SignalRecord> read() {
        try {
            String requestUrl = "http://mockServer:1080/api/signal?code=10969";

            // send http request to the mock server
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();

            // read the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                String responseBody = response.body();

                // parse the response body
                JsonNode signalJson = mapper.readTree(responseBody);
                Map<String, Object> additionalData = signalJson.has("additionalData") ? mapper.convertValue(signalJson.get("additionalData"), new TypeReference<>() {}) : new HashMap<>();
                String id = signalJson.get("id").asText();
                String type = signalJson.get("type").asText();
                String data = signalJson.get("data").toString();
                SignalRecord signal = new SignalRecord(id, type, data, additionalData);

                log.info("Recorded signal event '{}' ", signal);

                // process the signal
                SIGNALS.add(signal);
            } else {
                log.warn("Error while reading signaling events from endpoint: {}", response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Exception while preparing to process the signal '{}' from the endpoint", e.getMessage());
            e.printStackTrace();
        }
        return SIGNALS;
    }

    @Override
    public void close() {

    }
}
