package cz.todr.brewery.core;

import cz.todr.brewery.core.heating.api.Heating;
import cz.todr.brewery.core.temperature.api.Temperature;
import cz.todr.brewery.core.temperature.impl.TemperatureChangeRate;
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
	private TemperatureChangeRate temperatureChangeRate;

	@PostConstruct
	private void init() {
		Package pkg = this.getClass().getPackage(); 
		log.info("Project {}, version {}", pkg.getImplementationTitle(),pkg.getImplementationVersion());
	}
	
	@Override
	public float getTemp() {
		return SingleThreadedExecutor.executeAndAwait(thermometer::getTemp);
	}
	
	@Override
	public Float getRequiredTemp() {
		return SingleThreadedExecutor.executeAndAwait(temperature::getRequestedTemperature);
	}
	
	@Override
	public boolean isHeating() {
		return SingleThreadedExecutor.executeAndAwait(heating::isHeating);
	}

	@Override
	public Float getTemperatureChangeRate() {
		return SingleThreadedExecutor.executeAndAwait(() -> temperatureChangeRate.getTemperatureChangeRate());
	}

	@Override
	public void setRequiredTemp(Float temp) {
		log.info("New required temp: {}", temp);
		SingleThreadedExecutor.executeAndAwait(() -> temperature.requestTemperature(temp));
	}

	@Override
	public void registerButtonListener(ButtonStateListener listener) {
		SingleThreadedExecutor.executeAndAwait(() -> hardware.registerButtonListener(listener));
	}

	@Override
	public void display(String text) {
		val lines = text.split("\n");
		SingleThreadedExecutor.executeAndAwait(() -> hardware.display(lines[0], lines.length > 1 ? lines[1] : ""));
	}
}
