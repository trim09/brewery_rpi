package cz.todr.brewery.core.hardware;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.todr.brewery.core.hardware.raspberry.RaspberryInfo;
import cz.todr.brewery.core.hardware.raspberry.Relay;
import cz.todr.brewery.core.hardware.raspberry.Wire1Thermometer;

@Named
@Singleton
public class HardwareRaspberryImpl implements Hardware {
	private static final Logger LOG = LoggerFactory.getLogger(HardwareRaspberryImpl.class);
	
	@Inject
	private Relay relay;
	
	@Inject
	private Wire1Thermometer wire1Thermometer;
	
	@PostConstruct
	private void init() {
		RaspberryInfo.printInfo();
	}
	
	@Override
	public boolean isCompatible() {
		return RaspberryInfo.isRunningOnRaspberryPi();
	}

	@Override
	public void printInfo() {
		RaspberryInfo.printInfo();
		
	}

	@Override
	public void setHeating(boolean on) {
		LOG.trace("Setting heating {}", on ? "ON" : "OFF");
		relay.setConnected(on);
		
	}

	@Override
	public float getTemp() {
		float temp = wire1Thermometer.getTempSingleDevice();
		LOG.trace("Reading temp={}", temp);
		return temp;
	}
}
