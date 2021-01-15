package com.fahdisa.scs;

import com.fahdisa.scs.config.MongoClientFactory;
import com.fahdisa.scs.core.ProductService;
import com.fahdisa.scs.core.UserService;
import com.fahdisa.scs.db.product.ProductStore;
import com.fahdisa.scs.db.user.UserStore;
import com.fahdisa.scs.health.DatabaseHealthCheck;
import com.fahdisa.scs.resources.ProductResource;
import com.fahdisa.scs.resources.UserResource;
import com.mongodb.client.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class ShoppingCartServiceApplication extends Application<ShoppingCartServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ShoppingCartServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "ShoppingCartService";
    }

    @Override
    public void initialize(final Bootstrap<ShoppingCartServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<ShoppingCartServiceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ShoppingCartServiceConfiguration shoppingCartServiceConfiguration) {
                return shoppingCartServiceConfiguration.getSwaggerBundleConfiguration();
            }
        });
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public void run(final ShoppingCartServiceConfiguration configuration,
                    final Environment environment) {

        //Setup db connection
        MongoClientFactory mongoFactory = configuration.getMongoDatabaseClientFactory();
        MongoClient mongoClient = mongoFactory.build(environment);

        //Create Resource
        UserResource userResource = new UserResource(new UserService(
                new UserStore(mongoFactory.getName(), mongoClient)));
        ProductResource productResource = new ProductResource(new ProductService(
                new ProductStore(mongoFactory.getName(), mongoClient)));

        //Register resource
        environment.jersey().register(userResource);
        environment.jersey().register(productResource);

        //Register HealthCheck
        environment.healthChecks().register("database",
                new DatabaseHealthCheck(mongoFactory.getName(), mongoClient));
    }

}
