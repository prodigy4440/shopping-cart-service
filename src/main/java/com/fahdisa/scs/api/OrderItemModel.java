package com.fahdisa.scs.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemModel {

    @NotEmpty(message = "Product id is required")
    private String productId;

    @Min(value = 1, message = "Invalid quantity")
    private int quantity;

    public OrderItemModel() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemModel orderItemModel = (OrderItemModel) o;
        return quantity == orderItemModel.quantity && Objects.equals(productId, orderItemModel.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
