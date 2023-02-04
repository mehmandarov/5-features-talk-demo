package health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.Socket;

@Readiness
@ApplicationScoped
public class ReadinessHealthCheckSimple implements HealthCheck {
    @Inject
    @ConfigProperty(name = "BACKEND_SERVICE_HOST", defaultValue = "ws-backend")
    private String host;
    @Inject
    @ConfigProperty(name = "BACKEND_SERVICE_PORT", defaultValue = "8080")
    private int port;
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Readiness: Backend connection health check");
        try {
            pingServer(host, port);
            responseBuilder.up();
        } catch (Exception e) {
            responseBuilder.down()
                    .withData("error", e.getMessage());
        }
        return responseBuilder.build();
    }
    private void pingServer(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        socket.close();
    }
}