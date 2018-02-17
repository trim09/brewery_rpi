package cz.todr.brewery.core.hardware;

public interface Hardware {

	boolean isCompatible();
	
	void printInfo();
	
	void setHeating(boolean on);
	
	float getTemp();
}
