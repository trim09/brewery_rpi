package cz.todr.brewery.core.system.heating;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.hardware.Hardware;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;

@Named
public class HeatingImpl implements Heating {
	private static final Logger LOG = LoggerFactory.getLogger(HeatingImpl.class);
	
	@Inject
	private Config config;
	
	@Inject
	private SingleThreadedExecutor executor;
	
	@Inject
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
