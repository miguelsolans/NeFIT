/**
 * NeFIT
 * Authors
 * Notes: Application Entry Point
 */

import Controller.ItemOrderController;
import Controller.ItemProductionController;
import Controller.NegotiatorController;
import Controller.UserController;
import core.Template;
import health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class NeFIT extends Application<CompanyConfiguration> {

    @Override
    public void run(CompanyConfiguration configuration, Environment environment) throws Exception {

        final Template template = configuration.buildTemplate();
        environment.healthChecks().register("template", new TemplateHealthCheck(template));
        environment.jersey().register(new UserController(environment.getValidator()));
        environment.jersey().register(new ItemProductionController(environment.getValidator()));
        environment.jersey().register(new ItemOrderController(environment.getValidator()));
        environment.jersey().register(new NegotiatorController(environment.getValidator()));
    }

    @Override
    public void initialize(Bootstrap<CompanyConfiguration> b) {

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Running...");

        new NeFIT().run("server", "config.yml");
    }


}
