package cz.todr.brewery;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizerBeanPostProcessor;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		
		SpringApplication.run(Main.class, args);
	}

}
