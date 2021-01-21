package com.fahdisa.scs.resources;

import com.fahdisa.scs.api.ChangePassword;
import com.fahdisa.scs.api.Login;
import com.fahdisa.scs.core.UserService;
import com.fahdisa.scs.db.user.User;
import io.swagger.annotations.Api;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(
        tags = {"User Resource"}
)
@Path("/v1/user")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

//    @PermitAll
    @POST
    public Response create(@Valid User user) {
        return Response.ok(userService.create(user)).build();
    }

//    @PermitAll
    @Path("login")
    @POST
    public Response login(@Valid Login login) {
        return Response.ok(userService.login(login)).build();
    }

    @PermitAll
    @Path("/{id}")
    @GET
    public Response find(@PathParam("id") String id) {
        return Response.ok(userService.find(id)).build();
    }

    @Path("/update/password")
    @PUT
    public Response updatePassword(@Valid ChangePassword changePassword) {
        return Response.ok(userService.updatePassword(null, changePassword)).build();
    }

    @RolesAllowed("{ALL}")
    @Path("find")
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("size") @DefaultValue("20") Integer size) {
        return Response.ok(userService.findAll(page, size)).build();
    }

}
