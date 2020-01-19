
import core.Template;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class CompanyConfiguration extends Configuration {

    @org.hibernate.validator.constraints.NotEmpty
    private String template;

    private String defaultName = "nefit";

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Template buildTemplate() {
        return new Template(template, defaultName);
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String name) {
        this.defaultName = name;
    }



}
