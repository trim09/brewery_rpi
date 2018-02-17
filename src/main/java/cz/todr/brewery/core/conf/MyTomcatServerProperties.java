package cz.todr.brewery.core.conf;

import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class MyTomcatServerProperties extends ServerProperties {

    @Inject
    private Config config;

    @PostConstruct
    private void init() {
        setPort(config.getWebPort());
    }
}
