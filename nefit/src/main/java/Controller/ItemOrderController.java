/**
 * ItemOrderController
 * Authors:
 * Notes: Item Order Server Routes
 */
package Controller;

import db.ItemOrderDB;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("itemorder")
@Produces(MediaType.APPLICATION_JSON)
public class ItemOrderController {

    private Validator validator;

    public ItemOrderController(Validator validator) {
        this.validator = validator;
    }


    @GET
    @Path("/")
    public Response getOrders() {
        return Response.ok(ItemOrderDB.getOrders()).build();
    }

    // @POST
    // @Path("/")
    // public Response addOrder(

    // ) {
    //     return Response.ok().build();
    // }
}
