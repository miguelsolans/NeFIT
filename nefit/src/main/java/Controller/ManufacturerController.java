package Controller;

import business.Manufacturer;
import db.ManufacturerDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("manufacturers")
@Produces(MediaType.APPLICATION_JSON)
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

    @POST
    @Path("/")
    public Response newManufacturer(
            @NotNull @QueryParam("name") String name
    ) {
        Manufacturer manufacturer = new Manufacturer(name);

        ManufacturerDB.addManufacturer(manufacturer);

        return Response.ok().build();
    }
}
