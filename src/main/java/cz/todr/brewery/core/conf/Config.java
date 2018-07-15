package cz.todr.brewery.core.conf;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;

import java.time.Duration;

@Controller
@ConfigurationProperties(prefix = "app")
@Data
@Getter
public class Config {

	private float maximumHeatingSpeed;
	private Duration periodicLogRate;
}
