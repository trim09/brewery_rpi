package cz.todr.brewery.core.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class MyTomcatServerProperties extends ServerProperties {

    @Autowired
    private Config config;

    @PostConstruct
    private void init() {
        setPort(config.getWebPort());
    }
}
