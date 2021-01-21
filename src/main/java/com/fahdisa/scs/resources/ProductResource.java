package com.fahdisa.scs.resources;

import com.fahdisa.scs.api.ApiResponse;
import com.fahdisa.scs.api.Status;
import com.fahdisa.scs.core.ProductService;
import com.fahdisa.scs.db.product.Product;
import io.swagger.annotations.Api;

import javax.annotation.security.PermitAll;
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
import java.util.List;
import java.util.Optional;

@Api(
        tags = {"Product Resource"}
)
@Path("/v1/product/")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ProductResource {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PermitAll
    @POST
    public Response create(@Valid Product product) {
        Product created = productService.create(product);
        return Response.ok(
                new ApiResponse.Builder<Product>()
                        .status(Status.SUCCESS)
                        .description("Success")
                        .data(created)
                        .build()
        ).build();
    }

    @PermitAll
    @Path("/{id}")
    @GET
    public Response find(@PathParam("id") String id) {
        Optional<Product> optional = productService.find(id);
        if (!optional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse.Builder<Product>()
                                    .status(Status.FAILED)
                                    .description("Product not found")
                                    .build()
                    ).build();
        }
        return Response.ok(
                new ApiResponse.Builder<Product>()
                        .status(Status.SUCCESS)
                        .description("Success")
                        .data(optional.get())
                        .build()
        ).build();
    }

    @PermitAll
    @Path("search")
    @GET
    public Response search(@QueryParam("name") String name,
                           @QueryParam("page") @DefaultValue("0") int page,
                           @QueryParam("size") @DefaultValue("20") int size) {
        List<Product> products = productService.search(name, page, size);
        ApiResponse apiResponse = new ApiResponse.Builder<List<Product>>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(products)
                .build();
        return Response.ok(apiResponse).build();
    }

    @PermitAll
    @Path("/find")
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("size") @DefaultValue("20") Integer size) {
        List<Product> products = productService.findAll(page, size);
        ApiResponse apiResponse = new ApiResponse.Builder<List<Product>>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(products)
                .build();
        return Response.ok(apiResponse).build();
    }
}
