package cz.todr.brewery.core;

import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.system.heating.Heating;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Controller
public class BreweryCore {
	private static final Logger LOG = LoggerFactory.getLogger(BreweryCore.class);
	
	@Autowired
	private Heating heating;

	@Autowired
	private Hardware thermometer;
	
	@Autowired
	private ControlLoop tempController;
	
	@Autowired
	private SingleThreadedExecutor executor;
	
	@PostConstruct
	private void init() {
		Package pkg = this.getClass().getPackage(); 
		LOG.info("Project {}, version {}", pkg.getImplementationTitle(),pkg.getImplementationVersion());
	}
	
	public float getTemp() {
		return executor.executeAndAwait(thermometer::getTemp);
	}
	
	public float getRequiredTemp() {
		return executor.executeAndAwait(tempController::getRequiredTemp);
	}
	
	public boolean isHeating() {
		return executor.executeAndAwait(heating::isHeating);
	}
	
	public void setHeating(boolean on) {
		setTemp(on ? Float.MAX_VALUE : Float.MIN_VALUE);
	}
	
	public void setTemp(float temp) {
		LOG.info("New required temp: {}", temp);
		executor.executeAndAwait(() -> tempController.setRequiredTemp(temp));
	}
	
	public void stop() {
		LOG.info("Stopping Brewery");
		executor.shutdownNow();
		try {
			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOG.error("Could not stop Brewery core", e);
		}
	}
}
