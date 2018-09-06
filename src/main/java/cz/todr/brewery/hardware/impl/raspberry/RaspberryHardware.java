package cz.todr.brewery.hardware.impl.raspberry;

import com.pi4j.component.lcd.LCD;
import cz.todr.brewery.NotOnRaspberryException;
import cz.todr.brewery.hardware.api.ButtonStateListener;
import cz.todr.brewery.hardware.api.Hardware;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RaspberryHardware implements Hardware {

	private Relay relay;
	
	private Wire1Thermometer wire1Thermometer;

	private LCD lcd;

	private Buttons buttons;

	public RaspberryHardware() {
		if (!RaspberryInfo.isRunningOnRaspberryPi()) {
			throw new NotOnRaspberryException();
		}

		RaspberryInfo.printInfo();
		lcd = Display.getDisplay();
		wire1Thermometer = new Wire1Thermometer();
		relay = new Relay();
		buttons = new Buttons();
	}

	@Override
	public void setHeating(boolean on) {
		relay.setConnected(on);
	}

	@Override
	public float getTemp() {
		return wire1Thermometer.getTempSingleDevice();
	}

	@Override
	public void display(String firstRow, String secondRow) {
		lcd.writeln(0, firstRow);
		lcd.writeln(1, secondRow);
	}

	@Override
	public void registerButtonListener(ButtonStateListener listener) {
		buttons.registerListener(listener);
	}

	@Override
	public void setCursorOff() {
		lcd.setCursorHome();
	}

	@Override
	public void setCursorAt(int row, int column) {
		lcd.setCursorPosition(row, column);
	}
}
