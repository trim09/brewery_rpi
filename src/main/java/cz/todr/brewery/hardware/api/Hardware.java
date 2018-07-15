package cz.todr.brewery.hardware.api;

public interface Hardware {

	void setHeating(boolean on);
	
	float getTemp();

	void display(String firstRow, String secondRow);

	void registerButtonListener(ButtonEnum button, ButtonStateListener listener);
}
