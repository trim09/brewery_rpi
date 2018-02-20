package cz.todr.brewery.core.temperature;

import cz.todr.brewery.core.temperature.model.TemperatureHistoryRecord;

public interface TemperatureHistory {

	TemperatureHistoryRecord getOldestEntry();

}