package com.fahdisa.scs.db.order;

import com.fahdisa.scs.db.util.ObjectIdJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.mongojack.Id;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @Id
    @org.mongojack.ObjectId
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    private String userId;

    private Set<OrderItem> orderItems;

    private BigDecimal total;

    private Status status;

    private String requestId;

    private Date createdAt;

    private Date updatedAt;

    public Order() {
    }

    public Order(String userId, Set<OrderItem> orderItems, Status status, String requestId) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.total = new BigDecimal(0);
        this.status = status;
        this.requestId = requestId;
        this.createdAt = new Date();
        this.updatedAt = new Date();

        orderItems.forEach( orderItem -> {
            this.total.add(orderItem.getTotal());
        });
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(userId, order.userId) && Objects.equals(orderItems, order.orderItems) && Objects.equals(total, order.total) && status == order.status && Objects.equals(requestId, order.requestId) && Objects.equals(createdAt, order.createdAt) && Objects.equals(updatedAt, order.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, orderItems, total, status, requestId, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", orderItems=" + orderItems +
                ", total=" + total +
                ", status=" + status +
                ", requestId='" + requestId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public enum Status {
        PENDING, READY, COMPLETED, CANCELLED
    }
}
