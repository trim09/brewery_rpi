package cz.todr.brewery.core.system.temperature;

import java.time.Instant;

import cz.todr.brewery.core.system.temperature.entity.TemperatureHistoryRecord;

public interface TemperatureHistory {

	TemperatureHistoryRecord getOldestEntry();

	void add(Instant when, float temp);

}