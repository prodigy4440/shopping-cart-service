package com.fahdisa.scs.db.order;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {

    private String id;

    private BigDecimal price;

    private int quantity;

    private BigDecimal total;

    public OrderItem() {
    }

    public OrderItem(String id, int quantity, BigDecimal price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.total = price.multiply(new BigDecimal(quantity));
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && Objects.equals(id, orderItem.id)
                && Objects.equals(price, orderItem.price)
                && Objects.equals(total, orderItem.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, quantity, total);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }
}
