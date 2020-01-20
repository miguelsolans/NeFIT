/**
 * NeFIT
 * Authors
 * Notes: Application Entry Point
 */

import Controller.*;
import core.Cors;
import core.Template;
import health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class NeFIT extends Application<NefitConfiguration> {

    @Override
    public void run(NefitConfiguration configuration, Environment environment) {

        Cors.insecure(environment); // Check if works with Frontend with and without Cors

        final Template template = configuration.buildTemplate();
        environment.healthChecks().register("template", new TemplateHealthCheck(template));

        // Routes
        environment.jersey().register(new NegotiationController(environment.getValidator()));
        environment.jersey().register(new ItemProductionController(environment.getValidator()));
        environment.jersey().register(new ManufacturerController(environment.getValidator()));
        environment.jersey().register(new ImporterController(environment.getValidator()));
        environment.jersey().register(new NegotiationController(environment.getValidator()));

    }

    @Override
    public void initialize(Bootstrap<NefitConfiguration> b) {

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Catalog is Starting...");

        // new NeFIT().run("server", "config.yml");
        new NeFIT().run("server", "config.yml");
    }


}
