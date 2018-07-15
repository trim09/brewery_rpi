package cz.todr.brewery.hardware.impl;

import cz.todr.brewery.hardware.api.ButtonEnum;
import cz.todr.brewery.hardware.api.ButtonStateListener;
import cz.todr.brewery.hardware.api.Hardware;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@AllArgsConstructor
public class HardwareDecorator implements Hardware {

    private Hardware hardware;

    public void setHeating(boolean on) {
        log.trace("Setting heating {}", on ? "ON" : "OFF");
        hardware.setHeating(on);
    }

    public float getTemp() {
        val temp = hardware.getTemp();
        log.trace("Reading temp={}", temp);
        return temp;
    }

    public void display(String firstRow, String secondRow) {
        hardware.display(firstRow, secondRow);
    }

    public void registerButtonListener(ButtonEnum button, ButtonStateListener listener) {
        hardware.registerButtonListener(button, listener);
    }
}
