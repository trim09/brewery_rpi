package cz.todr.brewery.core.impl.heating.impl;

import cz.todr.brewery.hardware.api.Hardware;
import cz.todr.brewery.core.impl.heating.api.Heating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class HeatingImpl implements Heating {

	@Autowired
	private Hardware hardware;

	private boolean requestedState;

	private boolean heating;
	
	@Override
	public boolean isHeating() {
		return heating;
	}

	@Override
	public void setHeating(boolean on) {
		requestedState = on;
	}

	@Scheduled(fixedRateString="${app.relay_switching_max_frequency}")
	private void updateState() {
		heating = requestedState;
		hardware.setHeating(heating);
	}
}
