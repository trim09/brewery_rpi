package cz.todr.brewery.hardware.impl.raspberry;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import cz.todr.brewery.NotOnRaspberryException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Relay {

    private GpioPinDigitalOutput pin;

    public void Relay() {
		pin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_24, "heating_relay", PinState.HIGH);
    	if (pin == null) {
			throw new NotOnRaspberryException();
		}

		setConnected(false);
    }


	public void setConnected(boolean connected) {
		pin.setState(connected);
    }
}
