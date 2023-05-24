import data.MyMessage;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


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
