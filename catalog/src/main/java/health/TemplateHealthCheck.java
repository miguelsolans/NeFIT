package health;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Optional;
import core.Template;

public class TemplateHealthCheck extends HealthCheck {

    private final Template template;

    public TemplateHealthCheck(Template template) {
        this.template = template;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        template.render(Optional.of("woo"));
        template.render(Optional.absent());
        return HealthCheck.Result.healthy();
    }

}
