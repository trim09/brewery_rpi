package cz.todr.brewery.core.system.temperature;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.system.temperature.entity.TemperatureHistoryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

@Controller
public class TemperatureHistoryImpl implements TemperatureHistory {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureHistoryImpl.class);
	
	private final LinkedList<TemperatureHistoryRecord> records = new LinkedList<>();
		
	@Autowired
	private Config config;
	
	@Override
	public TemperatureHistoryRecord getOldestEntry() {
		TemperatureHistoryRecord oldest = records.peekFirst();
		LOG.trace("Oldest temperature record: {}", oldest);
		return oldest;
	}
	
	@Override
	public void add(Instant when, float temp) {
		LOG.trace("Recording temp {} at {}", temp, when);
		TemperatureHistoryRecord newest = records.peekLast();
		
		records.add(new TemperatureHistoryRecord(when, temp));

		if (newest != null && when.isBefore(newest.getTime())) {
			LOG.warn("Adding older temperature then the newest in history. Need to place it at correct place.");
			Collections.sort(records, Comparator.comparing(TemperatureHistoryRecord::getTime));
		}
		
		Duration historyLength = config.getTemperatureHistoryLength();
		Instant oldest = Instant.now().minus(historyLength);
		for (Iterator<TemperatureHistoryRecord> iterator = records.iterator(); iterator.hasNext();) {
			TemperatureHistoryRecord temperatureHistoryRecord = iterator.next();
			if (temperatureHistoryRecord.getTime().isBefore(oldest)) {
				iterator.remove();
			} else {
				break;
			}
		}
	}
	
}
