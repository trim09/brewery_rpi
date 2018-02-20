package cz.todr.brewery.core.hardware;

import com.pi4j.component.lcd.LCD;
import cz.todr.brewery.core.hardware.raspberry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class HardwareRaspberryImpl implements Hardware {
	private static final Logger LOG = LoggerFactory.getLogger(HardwareRaspberryImpl.class);

	private Relay relay;
	
	private Wire1Thermometer wire1Thermometer;

	private LCD lcd;

	private Buttons buttons;

	@PostConstruct
	private void init() {
		RaspberryInfo.printInfo();
		lcd = DisplayFactory.getDisplay();
		wire1Thermometer = new Wire1Thermometer();
		relay = new Relay();
		buttons = new Buttons();
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

	@Override
	public void display(String firstRow, String secondRow) {
		lcd.writeln(0, firstRow);
		lcd.writeln(1, secondRow);
	}

	@Override
	public void registerButtonListener(Buttons.ButtonEnum button, Buttons.ButtonStateListener listener) {
		buttons.registerListener(button, listener);
	}
}
