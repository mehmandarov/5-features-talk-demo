package health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Liveness
@ApplicationScoped
public class LivenessHealthCheckResponseTime implements HealthCheck {


    @Inject
    @ConfigProperty(name = "FRONTEND_SERVICE_HOST", defaultValue = "localhost")
    private String host;
    @Inject
    @ConfigProperty(name = "FRONTEND_SERVICE_HOST", defaultValue = "8080")
    private int port;

    @Inject
    @ConfigProperty(name = "FRONTEND_SERVICE_PATH", defaultValue = "hi")
    private String path;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Liveness: Self health check: Response Time");

        try {
            long start = System.currentTimeMillis();
            int responseCode = getServerResponseCode(host, port, path);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;

            if (timeElapsed < 3000) { // Faster than 3 seconds
                responseBuilder.up();
            } else {
                responseBuilder.down().withData("error", "Liveness: Critical service /" + path + " is too slow.");
            }

        } catch (IOException e) {
            responseBuilder.down().withData("error", "Liveness: Critical service has stopped responding.");
        }

        return responseBuilder.build();
    }

    private int getServerResponseCode(String host, int port, String path) throws IOException {
        URL url = new URL(String.format("http://%s:%s/%s", host, port, path));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int code = connection.getResponseCode();
        return code;
    }
}