package com.fahdisa.scs;

import com.fahdisa.scs.auth.UserAuthenticator;
import com.fahdisa.scs.auth.UserCredentialAuthFilter;
import com.fahdisa.scs.auth.UserDefaultAuthenticator;
import com.fahdisa.scs.auth.UserAuthorizer;
import com.fahdisa.scs.auth.UserPrinciple;
import com.fahdisa.scs.config.MongoClientFactory;
import com.fahdisa.scs.core.KeyService;
import com.fahdisa.scs.core.OrderService;
import com.fahdisa.scs.core.ProductService;
import com.fahdisa.scs.core.UserService;
import com.fahdisa.scs.db.order.OrderStore;
import com.fahdisa.scs.db.product.ProductStore;
import com.fahdisa.scs.db.user.UserStore;
import com.fahdisa.scs.health.DatabaseHealthCheck;
import com.fahdisa.scs.resources.OrderResource;
import com.fahdisa.scs.resources.ProductResource;
import com.fahdisa.scs.resources.UserResource;
import com.fahdisa.scs.resources.util.Cors;
import com.mongodb.client.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.Arrays;
import java.util.EnumSet;

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

        //Create Service
        KeyService keyService = new KeyService();
        UserService userService = new UserService(new UserStore(mongoFactory.getName(), mongoClient), keyService);
        ProductService productService = new ProductService(new ProductStore(mongoFactory.getName(), mongoClient));
        OrderService orderService = new OrderService(new OrderStore(mongoFactory.getName(), mongoClient), productService);


        //Create Resource
        UserResource userResource = new UserResource(userService);
        ProductResource productResource = new ProductResource(productService);
        OrderResource orderResource = new OrderResource(orderService);

        //Register resource
        environment.jersey().register(userResource);
        environment.jersey().register(productResource);
        environment.jersey().register(orderResource);

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(UserPrinciple.class));
        environment.jersey().register(
                new AuthDynamicFeature(
                        new ChainedAuthFilter<>(
                                Arrays.asList(
                                        new UserCredentialAuthFilter.Builder<UserPrinciple>()
                                                .setAuthenticator(
                                                        new UserAuthenticator(userService, keyService))
                                                .setPrefix("Bearer")
                                                .setAuthorizer(new UserAuthorizer())
                                                .buildAuthFilter(),
                                        new UserCredentialAuthFilter.Builder<UserPrinciple>()
                                                .setAuthenticator(new UserDefaultAuthenticator())
                                                .setAuthorizer(new UserAuthorizer())
                                                .setRealm("SUPER SECRET STUFF")
                                                .buildAuthFilter())
                        )
                )
        );

        //Register HealthCheck
        environment.healthChecks().register("database",
                new DatabaseHealthCheck(mongoFactory.getName(), mongoClient));

        //Enable cors
        Cors.insecure(environment);
    }

}
