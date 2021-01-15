package com.fahdisa.scs.core;

import com.fahdisa.scs.api.ApiResponse;
import com.fahdisa.scs.api.Status;
import com.fahdisa.scs.db.product.Product;
import com.fahdisa.scs.db.product.ProductStore;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductService {

    private final ProductStore productStore;

    public ProductService(ProductStore productStore) {
        this.productStore = productStore;
    }

    public ApiResponse<Product> create(Product product) {
        productStore.save(product);
        return new ApiResponse.Builder<Product>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(product)
                .build();
    }


    public ApiResponse<Product> find(String id) {
        if (StringUtils.isBlank(id)) {
            return new ApiResponse.Builder<Product>()
                    .status(Status.FAILED)
                    .description("Invalid Product Id")
                    .build();
        }
        Optional<Product> optional = productStore.find(id);
        if (!optional.isPresent()) {
            return new ApiResponse.Builder<>()
                    .status(Status.FAILED)
                    .description("Product not found")
                    .build();
        }
        return new ApiResponse.Builder<Product>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(optional.get())
                .build();
    }

    public ApiResponse<List<Product>> findAll(int page, int size) {
        if (page < 0) {
            page = 0;
        }
        if (size < 1) {
            size = 1;
        }
        List<Product> products = productStore.findAll(page, size);
        return new ApiResponse.Builder<List<Product>>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(products)
                .build();
    }


}
