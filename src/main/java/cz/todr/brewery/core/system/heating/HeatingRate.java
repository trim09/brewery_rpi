package cz.todr.brewery.core.system.heating;

import java.time.Instant;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.system.temperature.TemperatureHistory;
import cz.todr.brewery.core.system.temperature.entity.TemperatureHistoryRecord;

@Named
public class HeatingRate {
	private static final Logger LOG = LoggerFactory.getLogger(HeatingRate.class);
	
	@Inject private TemperatureHistory history;
	@Inject private Hardware thermometer;
	
	public float getHeatingRate() {
		TemperatureHistoryRecord tempHistoryRecord = history.getOldestEntry();		
		if (tempHistoryRecord == null) {
			LOG.trace("Cannot calculate a heating rate. No temperature history.");
			return 0;
		}
		
		float tempOld = tempHistoryRecord.getTemp();
		Instant timeOld = tempHistoryRecord.getTime();
		
		float tempNow = thermometer.getTemp();
		Instant timeNew = Instant.now();
		
		
		float tempDiff = tempNow - tempOld;
		long timeDiffMs = timeNew.toEpochMilli() - timeOld.toEpochMilli();
		
		float ratePerMs = tempDiff / timeDiffMs;
		float ratePerMin = ratePerMs * 1000;
		
		LOG.trace("Calculated heating speed {}°C/min (temp {}@{} -> {}@{})", ratePerMin, tempOld, timeOld, tempNow, tempOld);
		
		return ratePerMin;
	}
}
