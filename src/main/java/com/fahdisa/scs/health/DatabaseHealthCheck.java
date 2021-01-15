package com.fahdisa.scs.health;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoClient;
import org.bson.Document;

import java.util.Objects;

public class DatabaseHealthCheck extends HealthCheck {

    private MongoClient mongoClient;
    private String database;

    public DatabaseHealthCheck(String database, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.database = database;
    }

    @Override
    protected Result check() throws Exception {
        ListCollectionsIterable<Document> documents = mongoClient
                .getDatabase(database).listCollections();
        if (Objects.isNull(documents)) {
            return Result.unhealthy("Unable to get database collections");
        }
        return Result.healthy();
    }
}
