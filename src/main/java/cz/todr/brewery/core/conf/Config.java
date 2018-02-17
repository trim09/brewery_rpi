package cz.todr.brewery.core.conf;

import cz.todr.brewery.core.utils.aop.BeanModificationLogging;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@Controller
@BeanModificationLogging
@ManagedResource
public class Config {
	private final String persistqanceContextPropertyFileName = "persistance_context.properties";

	private volatile int webPort;

	private volatile float maximumHeatingSpeed;
	private volatile Duration maximumSwitchingSpeed;
	private volatile Duration controLoopPeriod;
	private volatile Duration temperatureRecordRate;
	private volatile boolean ignoreHeatingSpeed;
	private volatile Duration periodicLogRate;
	private volatile Duration temperatureHistoryLength;
	
	@PostConstruct
	public synchronized void init() throws IOException {
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("configuration.properties"));

		webPort = Integer.valueOf(prop.getProperty("web_port"));
		maximumHeatingSpeed = Float.valueOf(prop.getProperty("maximum_heating_speed"));
		maximumSwitchingSpeed = Duration.parse(prop.getProperty("max_relay_switchng_speed"));
		controLoopPeriod = Duration.parse(prop.getProperty("control_loop_period"));
		temperatureRecordRate = Duration.parse(prop.getProperty("temperature_record_rate"));
		ignoreHeatingSpeed = Boolean.valueOf(prop.getProperty("ignore_heating_speed"));
		periodicLogRate = Duration.parse(prop.getProperty("periodic_log_rate"));
		temperatureHistoryLength = Duration.parse(prop.getProperty("temperature_history_length"));
	}

	@ManagedAttribute
	public int getWebPort() {
		return webPort;
	}

	@ManagedAttribute
	public float getMaximumHeatingSpeed() {
		return maximumHeatingSpeed;
	}

	@ManagedAttribute
	public void setMaximumHeatingSpeed(float maximumHeatingSpeed) {
		this.maximumHeatingSpeed = maximumHeatingSpeed;
	}

	@ManagedAttribute
	public Duration getMaximumSwitchingSpeed() {
		return maximumSwitchingSpeed;
	}

	@ManagedAttribute
	public void setMaximumSwitchingSpeed(Duration maximumSwitchingSpeed) {
		this.maximumSwitchingSpeed = maximumSwitchingSpeed;
	}

	@ManagedAttribute
	public Duration getControLoopPeriod() {
		return controLoopPeriod;
	}

	@ManagedAttribute
	public void setControLoopPeriod(Duration controLoopPeriod) {
		this.controLoopPeriod = controLoopPeriod;
	}

	@ManagedAttribute
	public Duration getTemperatureRecordRate() {
		return temperatureRecordRate;
	}

	@ManagedAttribute
	public void setTemperatureRecordRate(Duration temperatureRecordRate) {
		this.temperatureRecordRate = temperatureRecordRate;
	}

	@ManagedAttribute
	public boolean isIgnoreHeatingSpeed() {
		return ignoreHeatingSpeed;
	}

	@ManagedAttribute
	public void setIgnoreHeatingSpeed(boolean ignoreHeatingSpeed) {
		this.ignoreHeatingSpeed = ignoreHeatingSpeed;
	}

	@ManagedAttribute
	public Duration getPeriodicLogRate() {
		return periodicLogRate;
	}

	@ManagedAttribute
	public void setPeriodicLogRate(Duration periodicLogRate) {
		this.periodicLogRate = periodicLogRate;
	}

	@ManagedAttribute
	public Duration getTemperatureHistoryLength() {
		return temperatureHistoryLength;
	}

	@ManagedAttribute
	public void setTemperatureHistoryLength(Duration temperatureHistoryLength) {
		this.temperatureHistoryLength = temperatureHistoryLength;
	}

	public String getPersistqanceContextPropertyFileName() {
		return persistqanceContextPropertyFileName;
	}

}
