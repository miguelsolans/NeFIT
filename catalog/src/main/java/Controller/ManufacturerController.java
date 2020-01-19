package Controller;

import business.Manufacturer;
import db.ManufacturerDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("manufacturer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManufacturerController {
    private Validator validator;


    public ManufacturerController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/")
    public Response getManufacturer() {
        return Response.ok(ManufacturerDB.getManufacturers()).build();
    }

    @GET
    @Path("{name}")
    public Response getSingleManufacturer(
            @NotNull @PathParam("name") String name
    ) {
        return Response.ok(ManufacturerDB.getSingleManufacturer(name)).build();
    }

    @GET
    @Path("{name}/active")
    public Response getActiveOffers(
            @NotNull @PathParam("name") String name
    ) {
        return Response.ok(ManufacturerDB.getAvailableProducts(name)).build();
    }

//    @PUT
//    @Path("{name}/{product}")
//    public Response updateProduct(
//            @NotNull @PathParam("name") String name,
//            @NotNull @PathParam("product") String product
//
//    ) {
//
//    }

    @POST
    @Path("/")
    public Response newManufacturer(
            @NotNull @QueryParam("name") String name,
            @NotNull @QueryParam("host") String host,
            @NotNull @QueryParam("port") String port
    ) {
        Manufacturer manufacturer = new Manufacturer(name, host, port);

        ManufacturerDB.addManufacturer(manufacturer);

        return Response.ok().build();
    }
}
