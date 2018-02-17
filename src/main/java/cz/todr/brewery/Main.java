package cz.todr.brewery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		
		SpringApplication.run(Main.class, args);
	}

}
