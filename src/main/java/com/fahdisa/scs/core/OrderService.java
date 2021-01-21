package com.fahdisa.scs.core;

import com.fahdisa.scs.api.OrderItemModel;
import com.fahdisa.scs.api.OrderRequest;
import com.fahdisa.scs.db.order.Order;
import com.fahdisa.scs.db.order.OrderItem;
import com.fahdisa.scs.db.order.OrderStore;
import com.fahdisa.scs.db.product.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class OrderService {

    private OrderStore orderStore;
    private ProductService productService;

    public OrderService(OrderStore orderStore, ProductService productService) {
        this.orderStore = orderStore;
        this.productService = productService;
    }

    public Order create(OrderRequest orderRequest) {
        Set<OrderItem> orderItems = createOrderItem(orderRequest.getItems());
        Order order = new Order(UUID.randomUUID().toString(), orderItems,
                Order.Status.PENDING, orderRequest.getRequestId());
        return orderStore.create(order);
    }

    public Order update(Order order) {
        return orderStore.update(order);
    }

    public Optional<Order> updateStatus(String orderId, Order.Status status) {

        Optional<Order> optionalOrder = find(orderId);
        if (!optionalOrder.isPresent()) {
            return Optional.empty();
        }

        Order order = optionalOrder.get();

        //A Cancel order cannot be updated
        if (order.getStatus() == Order.Status.CANCELLED) {
            return Optional.empty();
        }

        if (status == Order.Status.CANCELLED) {
            Set<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> {
                Optional<Product> optionalProduct = productService.find(orderItem.getId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                    productService.update(product);
                }
            });
        }

        order.setStatus(status);
        return Optional.of(update(order));
    }

    public Optional<Order> find(String orderId) {
        return orderStore.find(orderId);
    }

    public List<Order> findUserOrder(String userId, int page, int size) {
        return orderStore.findUserOrder(userId, page, size);
    }

    private OrderItem createOrderItem(OrderItemModel orderItemModel) {
        String productId = orderItemModel.getId();
        Optional<Product> optional = productService.find(productId);
        if (!optional.isPresent()) {
            return null;
        }

        Product product = optional.get();

        if (product.getQuantity() < orderItemModel.getQuantity()) {
            return null;
        }
        product.setQuantity(product.getQuantity() - orderItemModel.getQuantity());
        productService.update(product);

        OrderItem orderItem = new OrderItem(
                productId,
                orderItemModel.getQuantity(),
                product.getPrice()
        );

        return orderItem;
    }

    private Set<OrderItem> createOrderItem(Set<OrderItemModel> list) {
        Set<OrderItem> result = new HashSet<>();
        list.forEach(item -> {
            OrderItem orderItem = createOrderItem(item);
            if (Objects.nonNull(orderItem)) {
                result.add(orderItem);
            }
        });
        return result;
    }
}
