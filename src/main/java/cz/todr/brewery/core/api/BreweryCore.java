package cz.todr.brewery.core.api;

import cz.todr.brewery.hardware.api.ButtonStateListener;

import java.util.Optional;

public interface BreweryCore {
    float getTemp();

    Float getRequiredTemp();

    void setRequiredTemp(Float temp);

    boolean isHeating();

    void display(String text);

    void setCursorOff();

    void setCursorAt(int row, int column);

    void registerButtonListener(ButtonStateListener listener);

    Float getTemperatureChangeRate();
}
