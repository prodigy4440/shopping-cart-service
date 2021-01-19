package com.fahdisa.scs.core;

import com.fahdisa.scs.db.product.Product;
import com.fahdisa.scs.db.product.ProductStore;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductStore productStore;

    public ProductService(ProductStore productStore) {
        this.productStore = productStore;
    }

    public Product create(Product product) {
        return productStore.save(product);
    }

    public Product update(Product product) {
        return productStore.update(product);
    }

    public Optional<Product> find(String id) {
        return productStore.find(id);
    }


    public List<Product> search(String name, int page, int size) {
        return productStore.search(name, page, size);
    }

    public List<Product> findAll(int page, int size) {
        return productStore.findAll(page, size);
    }


}
