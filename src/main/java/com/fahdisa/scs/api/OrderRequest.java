package com.fahdisa.scs.api;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;

public class OrderRequest {

    @NotEmpty(message = "RequestId is required")
    private String requestId;

    private Set<OrderItemModel> items;

    public OrderRequest() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Set<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemModel> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(requestId, that.requestId) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, items);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "requestReference='" + requestId + '\'' +
                ", orderItems=" + items +
                '}';
    }
}
