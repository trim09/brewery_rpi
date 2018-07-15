package cz.todr.brewery.core.temperature;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.heating.api.Heating;
import cz.todr.brewery.core.temperature.api.Temperature;
import cz.todr.brewery.core.temperature.impl.TemperatureChangeRate;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import cz.todr.brewery.core.utils.Utils;
import cz.todr.brewery.hardware.api.Hardware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


@Slf4j
@Controller
public class TemperatureLogger {

	@Autowired
	private Config config;
	
	@Autowired
	private SingleThreadedExecutor executor;
	
	@Autowired
	private Hardware hw;
	
	@Autowired
	private TemperatureChangeRate temperatureChangeRate;
	
	@Autowired
	private Heating heating;

    @Autowired
    private Temperature temperature;

	@PostConstruct
	private void init() {
		long rateMs = config.getPeriodicLogRate().toMillis();	
		executor.scheduleAtFixedRate(this::log, 0, rateMs, TimeUnit.MILLISECONDS);
	}
	
	private void log() {
		try {
			log.info("Temp {} (diff {}°C/min), required temp {}, heating {}",
					Utils.formatFloat(hw.getTemp()),
					Utils.formatFloat(temperatureChangeRate.getTemperatureChangeRate()),
					Utils.formatFloat(temperature.getRequestedTemperature()),
					heating.isHeating() ? "ON" : "OFF");
		} catch (RuntimeException e) {
			log.error("Exception during periodic logging", e);
		}
	}
	
}
