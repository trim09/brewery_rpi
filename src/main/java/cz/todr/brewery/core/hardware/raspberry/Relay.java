package cz.todr.brewery.core.hardware.raspberry;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Named
@Singleton
public class Relay {
	private static final Logger LOG = LoggerFactory.getLogger(Relay.class);
	
    private GpioPinDigitalOutput pin;


    @PostConstruct
    private void init() {
    	if (RaspberryInfo.isRunningOnRaspberryPi()) {
	    	pin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_24, "heating_relay", PinState.HIGH);
	    	setConnected(false);
    	}  else {
    		LOG.error("Could not init PI4J library. Are you running it on RaspberryPi?");
    	}
    }
    
    
    public void setConnected(boolean connected) {
    	if (pin == null) {
    		LOG.error("Cannot set GPOI to {}. Are you running this app on RaspberryPi board?", connected ? "ON" : "OFF");
    	} else {
    		pin.setState(connected);
    	}
    }
}
