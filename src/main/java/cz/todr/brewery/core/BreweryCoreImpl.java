package cz.todr.brewery.core;

import cz.todr.brewery.core.heating.api.Heating;
import cz.todr.brewery.core.temperature.api.Temperature;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import cz.todr.brewery.hardware.api.ButtonStateListener;
import cz.todr.brewery.hardware.api.Hardware;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Slf4j
public class BreweryCoreImpl implements BreweryCore {
	@Autowired
	private Temperature temperature;

	@Autowired
	private Hardware thermometer;

	@Autowired
	private Heating heating;

	@Autowired
	private Hardware hardware;

	@Autowired
	private SingleThreadedExecutor executor;
	
	@PostConstruct
	private void init() {
		Package pkg = this.getClass().getPackage(); 
		log.info("Project {}, version {}", pkg.getImplementationTitle(),pkg.getImplementationVersion());
	}
	
	@Override
	public float getTemp() {
		return executor.executeAndAwait(thermometer::getTemp);
	}
	
	@Override
	public float getRequiredTemp() {
		return executor.executeAndAwait(temperature::getRequestedTemperature);
	}
	
	@Override
	public boolean isHeating() {
		return executor.executeAndAwait(heating::isHeating);
	}

	@Override
	public void setRequiredTemp(float temp) {
		log.info("New required temp: {}", temp);
		executor.executeAndAwait(() -> temperature.requestTemperature(temp));
	}

	@Override
	public void registerButtonListener(ButtonStateListener listener) {
		executor.executeAndAwait(() -> hardware.registerButtonListener(listener));
	}

	@Override
	public void display(String text) {
		val lines = text.split("\n");

		executor.executeAndAwait(() -> hardware.display(lines[0], lines.length > 1 ? lines[1] : ""));
	}
}
