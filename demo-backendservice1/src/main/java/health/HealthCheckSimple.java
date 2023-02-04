package health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Startup;

import javax.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class HealthCheckSimple implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Application started").up().build();
    }
}