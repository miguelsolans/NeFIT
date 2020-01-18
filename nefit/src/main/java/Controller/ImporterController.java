package Controller;

import business.Importer;
import business.ItemOrderOffer;
import db.ImporterDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("importer")
@Produces(MediaType.APPLICATION_JSON)
public class ImporterController {
    private Validator validator;

    public ImporterController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("{username}")
    public Response getImporter(
            @NotNull @PathParam("username") String name
    ) {
        Importer importer = ImporterDB.getSingleImporter(name);

        return Response.ok(importer).build();
    }

    @POST
    @Path("/")
    public Response newImporter(
            @NotNull @QueryParam("name") String name
    ) {
        ImporterDB.addImporter(new Importer(name));

        return Response.ok().build();
    }

    @POST
    @Path("/{username}")
    public Response newOrder(
            @NotNull @PathParam("username") String username,
            @NotNull @QueryParam("manufacturer") String manufacturer,
            @NotNull @QueryParam("product") String product,
            @NotNull @QueryParam("quantity") double quantity,
            @NotNull @QueryParam("price") double price
    ) {
        ItemOrderOffer order = new ItemOrderOffer(manufacturer, product, quantity, price);

        ImporterDB.newOrder(username, order);

        return Response.ok().build();
    }

}
