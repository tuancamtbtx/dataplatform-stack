package vn.dataplatform.cdc.pipeline.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.CommonConnectorConfig;
import io.debezium.pipeline.notification.Notification;
import io.debezium.pipeline.notification.channels.NotificationChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author tuan.nguyen3
 */
@Slf4j
public class HttpNotificationChannel implements NotificationChannel {
    public static final String CHANNEL_NAME = "http";
    private static final String NOTIFICATION_PREFIX = "[HTTP NOTIFICATION SERVICE]";

    @Override
    public void init(CommonConnectorConfig config) {

    }

    @Override
    public String name() {
        return CHANNEL_NAME;
    }

    @Override
    public void send(Notification notification) {
        log.info(String.format("%s Sending notification to http channel", NOTIFICATION_PREFIX));
        String binId = createBin();
        sendNotification(binId, notification);
    }

    @Override
    public void close() {

    }
    private static String createBin()  {
        // Create a bin on the server
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://www.toptal.com/developers/postbin/api/bin"))
                    .POST(HttpRequest.BodyPublishers.ofString(" "))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                String binId = response.body().replaceAll(".*\"binId\":\"([^\"]+)\".*", "$1");
                log.info("Bin created: " + response.body());
                return binId;
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static void sendNotification (String binId, Notification notification) {
        // Get notification from the bin
        try {
            ObjectMapper mapper = new ObjectMapper();
            String notificationString = mapper.writeValueAsString(notification);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://www.toptal.com/developers/postbin/" + binId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(notificationString))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("Notification received : " + response.body());
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
