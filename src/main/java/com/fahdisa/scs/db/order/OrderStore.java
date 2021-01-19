package com.fahdisa.scs.db.order;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.types.ObjectId;
import org.mongojack.JacksonMongoCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OrderStore {

    private final String collectionName = "order";
    private final JacksonMongoCollection<Order> collection;

    private final String CREATED_AT = "createdAt";
    private final String USER_ID = "userId";
    private final String REQUEST_ID = "requestId";
    private final String STATUS = "status";

    public OrderStore(String database, MongoClient mongoClient) {
        this.collection = JacksonMongoCollection.builder()
                .build(mongoClient, database, collectionName,
                        Order.class);
    }

    public Order create(Order order) {
        order.setCreatedAt(new Date());
        return update(order);
    }

    public Order update(Order order) {
        order.setUpdatedAt(new Date());
        collection.save(order);
        return order;
    }

    public Optional<Order> find(String id) {
        Order order = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(order);
    }

    public List<Order> findUserOrder(String userId, int page, int size) {
        return collection.find(Filters.eq(USER_ID, userId))
                .skip(page * size)
                .limit(size)
                .sort(Sorts.descending(CREATED_AT))
                .into(new ArrayList<>());
    }

    public List<Order> filter(String userId, String requestId, Order.Status status, Date createdAt, int page, int size) {
        return collection.find(
                Filters.or(
                        Filters.eq(USER_ID, userId),
                        Filters.gte(REQUEST_ID, requestId),
                        Filters.eq(STATUS, status),
                        Filters.gte(CREATED_AT, createdAt)
                )
        )
                .skip(page * size)
                .limit(size)
                .sort(Sorts.descending(CREATED_AT))
                .into(new ArrayList<>());
    }

}
