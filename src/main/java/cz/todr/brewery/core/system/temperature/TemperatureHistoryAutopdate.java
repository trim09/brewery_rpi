package cz.todr.brewery.core.system.temperature;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;

@Named
public class TemperatureHistoryAutopdate {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureHistoryAutopdate.class);
	
	@Inject
	private Hardware thermometer;
	
	@Inject 
	private SingleThreadedExecutor executor;
	
	@Inject
	private Config config;
	
	@Inject
	private TemperatureHistory history;
	
	@PostConstruct
	private void init() {
		long rateMs = config.getTemperatureRecordRate().toMillis();
		executor.scheduleAtFixedRate(this::update, 0, rateMs, TimeUnit.MILLISECONDS);
	}
	
	private void update() {
		try { 
			float temp = thermometer.getTemp();
			Instant when = Instant.now();
			history.add(when, temp);
		} catch (RuntimeException e) {
			LOG.error("Exception during temperature history autopdate", e);
		}
	}
	
}
