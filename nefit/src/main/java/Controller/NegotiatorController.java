package Controller;

import business.Negotiator;
import db.NegotiatorDB;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.Validator;

@Path("manufacturers")
@Produces(MediaType.APPLICATION_JSON)
public class NegotiatorController {

    private final Validator validator;

    public NegotiatorController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/{manufacturer}")
    public Response getManufacturer(
            @NotNull @PathParam("manufacturer") String manufacturer
    ) {
        return Response.ok(NegotiatorDB.getNegotiator(manufacturer)).build();
    }

    @POST
    @Path("/")
    public Response addManufacturer (
            @NotNull @QueryParam("manufacturer") String manufacturer,
            @NotNull @QueryParam("host") String host,
            @NotNull @QueryParam("port") String port
    ) {
        NegotiatorDB.addNegotiator(new Negotiator(manufacturer, host, port));
        return Response.ok().build();
    }
}
