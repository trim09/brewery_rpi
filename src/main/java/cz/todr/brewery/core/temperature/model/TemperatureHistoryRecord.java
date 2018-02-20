package cz.todr.brewery.core.temperature.model;

import lombok.Data;

import java.time.Instant;

@Data
public class TemperatureHistoryRecord {

	private final Instant time;
	
	private final float temp;
	
}
