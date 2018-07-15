package cz.todr.brewery.core;

import cz.todr.brewery.hardware.api.ButtonStateListener;

public interface BreweryCore {
    float getTemp();

    float getRequiredTemp();

    boolean isHeating();

    void setRequiredTemp(float temp);

    void display(String text);

    void registerButtonListener(ButtonStateListener listener);
}
