package Controller;

import business.Importer;
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
    @Path("{name}")
    public Response getImporter(
            @NotNull @PathParam("name") String name
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


}
