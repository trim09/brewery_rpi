package cz.todr.brewery.core.system.temperature.entity;

import java.time.Instant;

public class TemperatureHistoryRecord {

	private final Instant time;
	
	private final float temp;
	
	public TemperatureHistoryRecord(Instant time, float temp) {
		this.time = time;
		this.temp = temp;
	}

	public Instant getTime() {
		return time;
	}

	public float getTemp() {
		return temp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Float.floatToIntBits(this.temp);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemperatureHistoryRecord other = (TemperatureHistoryRecord) obj;
		if (Float.floatToIntBits(temp) != Float.floatToIntBits(other.temp))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TemperatureRecord [time=" + time + ", temp=" + temp + "]";
	}
	
}
