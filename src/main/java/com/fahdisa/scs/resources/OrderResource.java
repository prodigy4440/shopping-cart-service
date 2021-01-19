package com.fahdisa.scs.resources;

import com.fahdisa.scs.api.ApiResponse;
import com.fahdisa.scs.api.OrderRequest;
import com.fahdisa.scs.api.Status;
import com.fahdisa.scs.core.OrderService;
import com.fahdisa.scs.db.order.Order;
import io.swagger.annotations.Api;

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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Api(
        tags = {"Order Resource"}
)
@Path("/v1/order/")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @Path("create")
    @POST
    public Response create(@Valid OrderRequest orderRequest) {
        Order order = orderService.create(orderRequest);
        if (Objects.isNull(order)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(
                            new ApiResponse.Builder<Order>()
                                    .status(Status.FAILED)
                                    .description("unable to create request")
                                    .build()
                    ).build();
        }

        return Response.ok(
                new ApiResponse.Builder<Order>()
                        .status(Status.SUCCESS)
                        .description("Success")
                        .data(order)
                        .build()
        ).build();
    }

    @Path("{orderId}")
    @GET
    public Response find(@PathParam("orderId") String orderId) {
        Optional<Order> optional = orderService.find(orderId);
        if (!optional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse.Builder<Order>()
                                    .status(Status.FAILED)
                                    .description("Not found")
                                    .build()
                    ).build();
        }

        return Response.ok(
                new ApiResponse.Builder<Order>()
                        .status(Status.SUCCESS)
                        .description("Success")
                        .data(optional.get())
                        .build()
        ).build();
    }

    @Path("update/{orderId}")
    @PUT
    public Response updatesStatus(@PathParam("orderId") String orderId,
                                  @QueryParam("status") Order.Status status) {
        Optional<Order> order = orderService.updateStatus(orderId, status);
        if (!order.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(
                            new ApiResponse.Builder<Order>()
                                    .status(Status.FAILED)
                                    .description("Invalid Operation")
                                    .build()
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse.Builder<Order>()
                        .status(Status.SUCCESS)
                        .description("Success")
                        .data(order.get())
                        .build()
        ).build();
    }

    @Path("user/{userId}")
    @GET
    public Response find(@PathParam("userId") String userId,
                         @QueryParam("page") @DefaultValue("0") int page,
                         @QueryParam("size") @DefaultValue("20") int size) {
        List<Order> orders = orderService.findUserOrder(userId, page, size);
        return Response.ok(
                new ApiResponse.Builder<Order>()
                        .status(Status.SUCCESS)
                        .description("Success")
                        .data(orders)
                        .build()
        ).build();
    }
}
