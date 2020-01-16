
import io.dropwizard.Configuration;

import javax.validation.constraints.NotEmpty;

public class CompanyConfiguration extends Configuration {

    @NotEmpty
    public String template;

    public String defaultName = "Stranger";



}
