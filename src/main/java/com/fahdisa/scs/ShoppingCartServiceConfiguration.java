package com.fahdisa.scs;

import com.fahdisa.scs.config.MongoClientFactory;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class ShoppingCartServiceConfiguration extends Configuration {

    @JsonProperty("database")
    private MongoClientFactory mongoDatabaseClientFactory = new MongoClientFactory();


    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public MongoClientFactory getMongoDatabaseClientFactory() {
        return mongoDatabaseClientFactory;
    }

    public void setMongoDatabaseClientFactory(MongoClientFactory mongoDatabaseClientFactory) {
        this.mongoDatabaseClientFactory = mongoDatabaseClientFactory;
    }

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {
        this.swaggerBundleConfiguration = swaggerBundleConfiguration;
    }
}
