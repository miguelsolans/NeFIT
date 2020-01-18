/**
 * ItemOrderController
 * Authors:
 * Notes: Item Order Server Routes
 */
package Controller;

import db.ImporterDB;
import db.ItemOrderDB;
import db.ManufacturerDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("negotiation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NegotiationController {

    private Validator validator;

    public NegotiationController(Validator validator) {
        this.validator = validator;
    }


    @GET
    @Path("/")
    public Response getOrders() {
        return Response.ok(ManufacturerDB.getAllProducts()).build();
    }

    @GET
    @Path("/active")
    public Response getActiveOrders() {
        return Response.ok(ManufacturerDB.getAvailableProducts()).build();
    }

    @PUT
    @Path("/winner")
    public Response setWinner(
            @NotNull @QueryParam("user") String user,
            @NotNull @QueryParam("id") int orderId
    ) {

        ImporterDB.setWinner(user, orderId);
        return Response.ok().build();
    }
//    localhost:8080/negotiations/active -> todas as ativas
//    localhost:8080/negotiations/ -> historico de todas
}
