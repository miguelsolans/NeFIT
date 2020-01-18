package business;

import javax.validation.constraints.NotNull;

public class Negotiator {
    @NotNull
    String name;
    @NotNull
    String host;
    @NotNull
    String port;


    public Negotiator(String name, String host, String port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
