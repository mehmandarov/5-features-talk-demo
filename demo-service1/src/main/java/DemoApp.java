import data.MyMessage;
import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URISyntaxException;

@Path("/")
public class DemoApp {

    Unleash unleash;
    public DemoApp(){
        UnleashConfig config = UnleashConfig.builder()
                .appName("my.java-app")
                .instanceId("your-instance-1")
                .unleashAPI("http://unleash_web_1:4242/api")
                .apiKey("default:development.unleash-insecure-api-token")
                .build();

        unleash = new DefaultUnleash(config);
    }

    @PreDestroy
    void cleanUp(){
        unleash.shutdown();
    }

    @Inject
    @RestClient
    SomeWebServiceInterface service;

    @Path("/hi")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String entryPoint() throws URISyntaxException {
        MyMessage u = service.getThatData();  // <== Calls the "flickering" server-side service. Sometimes the service fails. ¯\_(ツ)_/¯
        return u.toString();
    }

    @Path("feature")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response featureToggle() {
        String message = "{ \"message\" : \"CLIENT: IT IS A BUG!\" }";
        if (unleash.isEnabled("feature")) {
            message = "{ \"message\" : \"CLIENT: IT IS A FEATURE!\" }";
        }
        return Response.ok(message).build();
    }
}

















