package com.fahdisa.scs;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final ShoppingCartServiceConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
