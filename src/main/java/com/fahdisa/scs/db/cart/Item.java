package com.fahdisa.scs.db.cart;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LineItem {
    @NotEmpty(message = "ProductId is required")
    private String id;
    
    @Min(value = 0, message = "Invalid quantity")
    private int quantity;

    @NotNull(message = "Total is required")
    private BigDecimal total;
}
