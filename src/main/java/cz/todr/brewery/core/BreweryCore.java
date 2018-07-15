package cz.todr.brewery.core;

import cz.todr.brewery.hardware.api.ButtonEnum;
import cz.todr.brewery.hardware.api.ButtonStateListener;

public interface BreweryCore {
    float getTemp();

    float getRequiredTemp();

    boolean isHeating();

    void setTemp(float temp);

    void registerButtonListener(ButtonEnum button, ButtonStateListener listener);
}
