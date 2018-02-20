package cz.todr.brewery.core.hardware;

import cz.todr.brewery.core.hardware.raspberry.Buttons;

public interface Hardware {

	void setHeating(boolean on);
	
	float getTemp();

	void display(String firstRow, String secondRow);

	void registerButtonListener(Buttons.ButtonEnum button, Buttons.ButtonStateListener listener);
}
