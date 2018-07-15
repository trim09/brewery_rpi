package cz.todr.brewery.hardware.api;

import cz.todr.brewery.hardware.api.ButtonEnum;

public interface ButtonStateListener {
    void stateChanged(ButtonEnum button, boolean pressed);
}
