package Controller;

import business.ItemProductionOffer;
import db.ItemProductionDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("b")
@Produces(MediaType.APPLICATION_JSON)
public class ItemProductionController {

    private Validator validator;

    public ItemProductionController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/")
    public Response getProducts() {
        return Response.ok(ItemProductionDB.getAvailableProducts()).build();
    }

    @GET
    @Path("{manufacturer}")
    public Response getManufacturerProducts(
            @NotNull @PathParam("manufacturer") String manufacturer
    ) {
        return Response.ok(ItemProductionDB.getManufacturerProducts(manufacturer)).build();
    }

    @POST
    @Path("/")
    public Response addProduct(
            @NotNull @QueryParam("productName") String productName,
            @NotNull @QueryParam("manufacturerName") String manufacturerName,
            @NotNull @QueryParam("unitPrice") double unitPrice,
            @NotNull @QueryParam("minimumAmout") double minimumAmout,
            @NotNull @QueryParam("maximumAmount") double maximumAmount,
            @NotNull @QueryParam("period") int period
    ) {
        ItemProductionOffer product = new ItemProductionOffer(productName, manufacturerName, unitPrice, minimumAmout, maximumAmount, period);

        ItemProductionDB.addProduct(product);

        return Response.ok().build();
    }


}
