package com.fahdisa.scs.db.product;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.types.ObjectId;
import org.mongojack.JacksonMongoCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProductStore {

    private final String collectionName = "product";
    private final JacksonMongoCollection<Product> collection;

    public ProductStore(String database, MongoClient mongoClient) {
        this.collection = JacksonMongoCollection.builder()
                .build(mongoClient, database, collectionName,
                        Product.class);
    }

    public Product save(Product product) {
        product.setCreatedAt(new Date());
        return update(product);
    }

    public Product update(Product product) {
        product.setUpdatedAt(new Date());
        collection.save(product);
        return product;
    }

    public List<Product> search(String search, int page, int size) {
        return collection.find(
                Filters.eq("name", search)
        ).skip(page * size).limit(size).into(new ArrayList<>());
    }

    public Optional<Product> find(String id) {
        Product product = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(product);
    }

    public List<Product> findAll(int page, int size) {
        return collection.find().sort(Sorts.descending("createdAt"))
                .skip(page * size).limit(size).into(new ArrayList<>());
    }
}
