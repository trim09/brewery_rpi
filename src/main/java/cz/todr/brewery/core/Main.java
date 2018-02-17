package cz.todr.brewery.core;

import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.conf.SpringConfiguration;

public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	/* For testing see http://dontpanic.42.nl/2011/07/advanced-unit-testing-with-your-spring.html */
		
/*	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.ENGLISH);
	
	    //StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
	    
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class)) {

			Cli.CmdArgs processedArgs = Cli.getProcessedArguments(args);
			LOG.info("Using args: {}", processedArgs);
			
			Config config = context.getBean(Config.class);
			if (processedArgs.isIgnoreHeatingSpeed()) {
				config.setIgnoreHeatingSpeed(true);
			} else {
				config.setMaximumHeatingSpeed(processedArgs.getMaxHeatingRate());
				config.setIgnoreHeatingSpeed(false);
			}
			
			BreweryCore brewery = context.getBean(BreweryCore.class);
			brewery.setTemp(processedArgs.getRequiredTemp());
			
			System.in.read();
		}
	}*/
}
