package com.fahdisa.scs.resources;

import com.fahdisa.scs.core.ProductService;
import com.fahdisa.scs.db.product.Product;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/product/")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ProductResource {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @POST
    public Response create(@Valid Product product) {
        return Response.ok(productService.create(product)).build();
    }

    @Path("/{id}")
    @GET
    public Response find(@PathParam("id") String id) {
        return Response.ok(productService.find(id)).build();
    }

    @Path("/find")
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("size") @DefaultValue("20") Integer size) {
        return Response.ok(productService.findAll(page, size)).build();
    }
}
