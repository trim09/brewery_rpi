package cz.todr.brewery.core;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.system.heating.Heating;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;

@Named
public class BreweryCore {
	private static final Logger LOG = LoggerFactory.getLogger(BreweryCore.class);
	
	@Inject
	private Heating heating;

	@Inject
	private Hardware thermometer;
	
	@Inject
	private ControlLoop tempController;
	
	@Inject
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
