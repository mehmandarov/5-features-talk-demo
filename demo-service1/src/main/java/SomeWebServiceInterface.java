import data.MyMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RegisterRestClient(baseUri = "http://ws-backend:8080/")
@Path("api/")
public interface SomeWebServiceInterface {

    @GET
    @Path("message/")
    @Produces(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod= "getThatOtherData")
    MyMessage getThatData();

    @GET
    @Path("message_backup/")
    @Produces(MediaType.APPLICATION_JSON)
    MyMessage getThatOtherData();

}
