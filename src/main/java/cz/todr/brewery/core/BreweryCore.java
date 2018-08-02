package cz.todr.brewery.core;

import cz.todr.brewery.hardware.api.ButtonStateListener;

public interface BreweryCore {
    float getTemp();

    Float getRequiredTemp();

    boolean isHeating();

    void setRequiredTemp(Float temp);

    void display(String text);

    void registerButtonListener(ButtonStateListener listener);

    Float getTemperatureChangeRate();
}
