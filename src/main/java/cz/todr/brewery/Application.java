package cz.todr.brewery;

import cz.todr.brewery.hardware.impl.raspberry.RaspberryInfo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);

		new SpringApplicationBuilder(Application.class)
				.headless(RaspberryInfo.isRunningOnRaspberryPi())
				.run(args);
	}
}
