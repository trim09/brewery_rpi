package cz.todr.brewery.core.heating.api;

public interface Heating {

	boolean isHeating();

	void requestHeatingState(boolean on);

}