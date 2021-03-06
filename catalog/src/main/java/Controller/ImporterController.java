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
@Consumes(MediaType.APPLICATION_JSON)
public class ImporterController {

    Validator validator;

    public ImporterController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/")
    public Response getImporters() {
        return Response.ok(ImporterDB.getImporters()).build();
    }

    @GET
    @Path("{name}")
    public Response getImporter (
            @NotNull @PathParam("name") String name
    ){
        return Response.ok(ImporterDB.getSingleImporter(name)).build();
    }

    @GET
    @Path("{name}/orders")
    public Response getImporterOrders (
            @NotNull @PathParam("name") String name
    ) {
        return Response.ok(ImporterDB.getImporterOrder(name)).build();
    }

    @POST
    @Path("/")
    public Response newImporter(
            @NotNull @QueryParam("name") String name
    ) {
        System.out.println("Name: " + name);
        ImporterDB.addImporter(new Importer(name));

        return Response.ok().build();
    }

    @POST
    @Path("{name}")
    public Response newImporterOrder(
            @NotNull @PathParam("name") String name,
            @NotNull @QueryParam("manufacturer") String manufacturer,
            @NotNull @QueryParam("product") String product,
            @NotNull @QueryParam("quantity") double quantity,
            @NotNull @QueryParam("price") double price,
            @NotNull @QueryParam("id") int id
    ) {
        ImporterDB.newOrder(name, new ItemOrderOffer(manufacturer, product, quantity, price, id));
        return Response.ok().build();
    }
}
