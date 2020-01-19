package Controller;

import business.ItemProductionOffer;
import db.ManufacturerDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemProductionController {

    private Validator validator;

    public ItemProductionController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/")
    public Response getProducts() {
        return Response.ok(ManufacturerDB.getAvailableProducts()).build();

    }

    @GET
    @Path("{manufacturer}")
    public Response getManufacturerProducts(
            @NotNull @PathParam("manufacturer") String manufacturer
    ) {
        return Response.ok(ManufacturerDB.getManufacturerProducts(manufacturer)).build();
    }

    @POST
    @Path("{manufacturer}")
    public Response addProduct(
            @NotNull @PathParam("manufacturer") String manufacturer,
            @NotNull @QueryParam("productName") String productName,
            @NotNull @QueryParam("unitPrice") double unitPrice,
            @NotNull @QueryParam("minimumAmount") double minimumAmount,
            @NotNull @QueryParam("maximumAmount") double maximumAmount,
            @NotNull @QueryParam("period") int period
    ) {
        ItemProductionOffer product = new ItemProductionOffer(productName, manufacturer, unitPrice, minimumAmount, maximumAmount, period);

        ManufacturerDB.addProduct(manufacturer, product);

        return Response.ok().build();
    }

    @PUT
    @Path("/close/{manufacturer}")
    public Response closeProduct(
            @NotNull @PathParam("manufacturer") String manufacturer,
            @NotNull @QueryParam("product") String product
    ) {
        ManufacturerDB.closeOffer(manufacturer, product);

        return Response.ok().build();
    }
}
