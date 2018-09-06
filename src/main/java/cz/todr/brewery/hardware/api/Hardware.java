package cz.todr.brewery.hardware.api;

public interface Hardware {

	void setHeating(boolean on);
	
	float getTemp();

	void display(String firstRow, String secondRow);

	void setCursorOff();

	void setCursorAt(int row, int column);

	void registerButtonListener(ButtonStateListener listener);
}
