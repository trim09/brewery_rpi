package cz.todr.brewery.core.temperature;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.heating.Heating;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import cz.todr.brewery.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


@Controller
public class TemperatureLogger {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureLogger.class);
	
	@Autowired
	private Config config;
	
	@Autowired
	private SingleThreadedExecutor executor;
	
	@Autowired
	private Hardware hw;
	
	@Autowired
	private TemperatureChangeRate heatingRate;
	
	@Autowired
	private Heating heating;
	
	@Autowired
	private TemperatureControlLoop controller;
	
	@PostConstruct
	private void init() {
		long rateMs = config.getPeriodicLogRate().toMillis();	
		executor.scheduleAtFixedRate(this::log, 0, rateMs, TimeUnit.MILLISECONDS);
	}
	
	private void log() {
		try {
			LOG.info("Temp {} (diff {}°C/min), required temp {}, heating {}",
					Utils.formatFloat(hw.getTemp()),
					Utils.formatFloat(heatingRate.getHeatingRate()),
					Utils.formatFloat(controller.getRequiredTemp()),
					heating.isHeating() ? "ON" : "OFF");
		} catch (RuntimeException e) {
			LOG.error("Exception during periodic logging", e);
		}
	}
	
}
