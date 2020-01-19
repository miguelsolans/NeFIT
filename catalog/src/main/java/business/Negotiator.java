package business;

import javax.validation.constraints.NotNull;

public class Negotiator {

    @NotNull
    private String host;
    @NotNull
    private String port;

    public Negotiator(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
