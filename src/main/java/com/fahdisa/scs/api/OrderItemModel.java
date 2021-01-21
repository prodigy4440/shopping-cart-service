package com.fahdisa.scs.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemModel {

    @NotEmpty(message = "Product id is required")
    private String id;

    @Min(value = 1, message = "Invalid quantity")
    @JsonProperty("quantitySelected")
    private int quantity;

    public OrderItemModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return quantity == orderItemModel.quantity && Objects.equals(id, orderItemModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId='" + id + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
