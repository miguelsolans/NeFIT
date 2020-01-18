/**
 * ItemOrderController
 * Authors:
 * Notes: Item Order Server Routes
 */
package Controller;

import db.ImporterDB;
import db.ItemOrderDB;

import javax.validation.Validator;
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
        return Response.ok(ImporterDB.getAllOrders()).build();
    }

    @GET
    @Path("/active")
    public Response getActiveOrders() {
        return Response.ok(ImporterDB.getActiveOrders()).build();
    }


//    localhost:8080/negotiations/active -> todas as ativas
//    localhost:8080/negotiations/ -> historico de todas
}
