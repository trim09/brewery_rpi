package cz.todr.brewery.hardware.api;

public interface ButtonStateListener {
    void stateChanged(ButtonEnum button, boolean pressed);
}
