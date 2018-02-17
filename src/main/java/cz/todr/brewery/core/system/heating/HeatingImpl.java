package cz.todr.brewery.core.system.heating;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@Controller
public class HeatingImpl implements Heating {
	private static final Logger LOG = LoggerFactory.getLogger(HeatingImpl.class);
	
	@Autowired
	private Config config;
	
	@Autowired
	private SingleThreadedExecutor executor;
	
	@Autowired
	private Hardware hardware;
	
	private long nextStateChange;
	
	private boolean requiredHeatingState;
	
	private boolean scheduledHeatingStateChange;

	private boolean heating;
	
	@Override
	public boolean isHeating() {
		return heating;
	}

	@Override
	public void setHeating(boolean on) {
		this.requiredHeatingState = on;
		LOG.trace("Heating change state request to {}", on ? "ON" : "OFF");
		
		if (!scheduledHeatingStateChange) {
			long now = System.currentTimeMillis();
			if (nextStateChange <= now) {
				setHeatingNow();
			} else {
				scheduledHeatingStateChange = true;
				long milis = nextStateChange - now;
				LOG.debug("Delayed heating state change {}ms", milis);
				executor.schedule(this::setHeatingNow, milis, TimeUnit.MILLISECONDS);
			}
		}
	}
	
	private void setHeatingNow() {
		scheduledHeatingStateChange = false;
		nextStateChange = System.currentTimeMillis() + config.getMaximumSwitchingSpeed().toMillis();
		
		heating = requiredHeatingState;
		hardware.setHeating(requiredHeatingState);
	}
	
}
