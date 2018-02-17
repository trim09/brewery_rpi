package cz.todr.brewery.core.system.temperature;

import cz.todr.brewery.core.system.temperature.entity.TemperatureHistoryRecord;

import java.time.Instant;

public interface TemperatureHistory {

	TemperatureHistoryRecord getOldestEntry();

	void add(Instant when, float temp);

}