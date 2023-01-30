import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;

import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.Random;

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


    @Path("/hi")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String entryPoint() throws URISyntaxException {
        String message = "{ \"message\" : \"Hello There from BACKEND!\" }";
        return message;
    }

    @Path("api/message")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response otherEntryPointWithErrors() {
        String message = "{ \"message\" : \"Hello There!\" }";

        int roll = 0;
        boolean success = true;
        Random  r = new Random();
        roll = r.nextInt(6);

        if (unleash.isEnabled("chaos")) {
            if (roll % 2 == 0) { // Fail at random... Roll a dice! :-)
                success = false;
            }
        }

        if (success) {
            return Response.ok(message).build();
        }

        return Response.serverError()
                .type(MediaType.APPLICATION_JSON)
                .entity("Some weird error.")
                .build();
    }

    /* MY BACKUP SERVICE */
    @Path("api/message_backup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response otherEntryPoint() {
        String message = "{ \"message\" : \"A BACKUP Hello!\" }";
        return Response.ok(message).build();
    }

    @Path("feature")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response featureToggle() {
        String message = "{ \"message\" : \"BACKEND: IT IS A BUG!\" }";

        if (unleash.isEnabled("feature")) {
            message = "{ \"message\" : \"BACKEND: IT IS A FEATURE!\" }";
        }

        return Response.ok(message).build();
    }
}
















