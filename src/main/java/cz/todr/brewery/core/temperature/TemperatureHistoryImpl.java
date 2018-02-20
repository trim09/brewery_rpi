package cz.todr.brewery.core.temperature;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.temperature.model.TemperatureHistoryRecord;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@Controller
public class TemperatureHistoryImpl implements TemperatureHistory {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureHistoryImpl.class);
	
	private final LinkedList<TemperatureHistoryRecord> records = new LinkedList<>();

	@Autowired
	private Hardware hw;

	@Autowired
	private SingleThreadedExecutor executor;

	@Autowired
	private Config config;


	@PostConstruct
	private void init() {
		long rateMs = config.getTemperatureRecordRate().toMillis();
		executor.scheduleAtFixedRate(this::collectTemperature, 0, rateMs, TimeUnit.MILLISECONDS);
	}

	private void collectTemperature() {
		try {
			add(Instant.now(), hw.getTemp());
		} catch (RuntimeException e) {
			LOG.error("Exception during temperature history autopdate", e);
		}
	}

	private void add(Instant when, float temp) {
		LOG.trace("Recording temp {} at {}", temp, when);

		records.add(new TemperatureHistoryRecord(when, temp));

		removeObsoleteRecords();
	}

	private void removeObsoleteRecords() {
		Instant oldest = Instant.now().minus(config.getTemperatureHistoryLength());
		records.removeIf(r -> r.getTime().isBefore(oldest));
	}

	@Override
	public TemperatureHistoryRecord getOldestEntry() {
		TemperatureHistoryRecord oldest = records.peekFirst();
		LOG.trace("Oldest temperature record: {}", oldest);
		return oldest;
	}
	

}
