package cz.todr.brewery.core.temperature;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.heating.Heating;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import cz.todr.brewery.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@Controller
public class TemperatureControlLoop {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureControlLoop.class);
	
	@Autowired
	private SingleThreadedExecutor executor;
	
	@Autowired
	private Config config;

	@Autowired
	private Heating heating;
	
	@Autowired
	private Hardware thermometer;

	@Autowired
	private TemperatureChangeRate heatingSpeed;

	private boolean running;
	
	private float requiredTemp;
	
	public void setRequiredTemp(float requiredTemp) {
		this.requiredTemp = requiredTemp;
		
		if (!running) {
			LOG.debug("Starting temperature control loop");
			running = true;
			executor.scheduleAtFixedRate(this::controlLoop, 0, config.getControLoopPeriod().toMillis(), TimeUnit.MILLISECONDS);
		}
	}

	public float getRequiredTemp() {
		return requiredTemp;
	}

	private void controlLoop() {
		try {
			float temp = thermometer.getTemp();
			float heatingspeed = heatingSpeed.getHeatingRate();
			boolean tempRegulation = temp < requiredTemp;
			boolean heatingSpeedRegulation = heatingspeed < config.getMaximumHeatingSpeed();
			boolean shouldTurnOnHeating = tempRegulation && (heatingSpeedRegulation || config.isIgnoreHeatingSpeed());
			
			if (LOG.isTraceEnabled()) {
				LOG.trace("temp={} (diff={}°C), requiredTemp={}, turnOnHeating={}, heatingSpeedOK={}",
						Utils.formatFloat(temp), 
						Utils.formatFloat(heatingspeed), 
						Utils.formatFloat(requiredTemp), 
						tempRegulation, heatingSpeedRegulation);
			}
			
			heating.setHeating(shouldTurnOnHeating);
		} catch (RuntimeException e) {
			LOG.error("Exception control loop", e);
		}
	}

}
