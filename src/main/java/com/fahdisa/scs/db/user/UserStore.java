package com.fahdisa.scs.db.user;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserStore {

    private final String collectionName = "user";
    private final MongoCollection<User> collection;

    public UserStore(String database, MongoClient mongoClient) {
        this.collection = mongoClient.getDatabase(database).getCollection(collectionName, User.class);
    }

    public User save(User user) {
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        collection.insertOne(user);
        return user;
    }

    public Optional<User> findById(String id) {
        ObjectId objectId = new ObjectId(id);
        User user = collection.find(Filters.eq("_id", objectId)).first();
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) {
        User user = collection.find(Filters.eq("email", email)).first();
        return Optional.ofNullable(user);
    }

    public boolean updatePassword(String id, String password) {
        UpdateResult updateResult = collection.updateOne(Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("password", password),
                        Updates.set("updatedAt", new Date())
                ));
        return updateResult.wasAcknowledged();
    }

    public List<User> findAll(int page, int size) {
        return collection.find().sort(Sorts.descending("createdAt"))
                .skip(page * size).limit(size).into(new ArrayList<>());
    }

}
