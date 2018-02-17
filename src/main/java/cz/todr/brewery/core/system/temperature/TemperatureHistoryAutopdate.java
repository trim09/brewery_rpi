package cz.todr.brewery.core.system.temperature;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Controller
public class TemperatureHistoryAutopdate {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureHistoryAutopdate.class);
	
	@Autowired
	private Hardware thermometer;
	
	@Autowired
	private SingleThreadedExecutor executor;
	
	@Autowired
	private Config config;
	
	@Autowired
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
