/**
 * User Controller
 * Authors:
 * Notes: REST Resource for Users
 */

package Controller;

import business.User;
import db.UserDB;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final Validator validator;

    public UserController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/")
    public Response getUsers() {
        return Response.ok(UserDB.getUsers()).build();
    }

    @GET
    @Path("/{username}")
    public Response getUser(
        @NotNull @QueryParam("username") String username
    ) {
        User user = UserDB.getUser(username);

        return Response.ok(user).build();
    }
    @POST
    @Path("/")
    public Response newUser(
            @NotNull @QueryParam("username") String username,
            @NotNull @QueryParam("password") String password
    ) {

        User u = UserDB.getUser(username);
        if(u == null) {
            System.out.println("Adding user: " + username);

            UserDB.addUser(username, password);
            return Response.ok().build();
        }

        return Response.notModified("").build();
    }

    @PUT
    @Path("{username}")
    public Response resetPassword(
            @NotNull @PathParam("username") String username,
            @NotNull @QueryParam("password") String password
    ) {
        User u = UserDB.getUser(username);

        if(u != null) {
            u.setPassword(password);
            return Response.ok().build();
        }
        return Response.notModified("teste").build();
    }
}
