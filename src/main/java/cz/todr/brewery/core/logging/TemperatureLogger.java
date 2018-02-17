package cz.todr.brewery.core.logging;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.todr.brewery.core.ControlLoop;
import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.system.heating.Heating;
import cz.todr.brewery.core.system.heating.HeatingRate;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import cz.todr.brewery.core.utils.Utils;


@Named
@Singleton
public class TemperatureLogger {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureLogger.class);
	
	@Inject
	private Config config;
	
	@Inject
	private SingleThreadedExecutor executor;
	
	@Inject
	private Hardware thermometer;
	
	@Inject
	private HeatingRate heatingRate;
	
	@Inject
	private Heating heating;
	
	@Inject
	private ControlLoop controller;
	
	@PostConstruct
	private void init() {
		long rateMs = config.getPeriodicLogRate().toMillis();	
		executor.scheduleAtFixedRate(this::log, 0, rateMs, TimeUnit.MILLISECONDS);
	}
	
	private void log() {
		try {
			LOG.info("Temp {} (diff {}°C/min), required temp {}, heating {}",
					Utils.formatFloat(thermometer.getTemp()), 
					Utils.formatFloat(heatingRate.getHeatingRate()),
					Utils.formatFloat(controller.getRequiredTemp()),
					heating.isHeating() ? "ON" : "OFF");
		} catch (RuntimeException e) {
			LOG.error("Exception during periodic logging", e);
		}
	}
	
}
