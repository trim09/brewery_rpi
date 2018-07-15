package cz.todr.brewery.hardware.impl.desktop;

import cz.todr.brewery.hardware.api.ButtonEnum;
import cz.todr.brewery.hardware.api.ButtonStateListener;
import cz.todr.brewery.hardware.api.Hardware;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DesktopHardwareSimulator implements Hardware {

    private final DesktopSwingHardware desktopSwingHardware = new DesktopSwingHardware();
    private final TemperatureSimulator temperatureSimulator = new TemperatureSimulator();

    public DesktopHardwareSimulator() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
                runnable -> new Thread(runnable, "DesktopHardwareSimulator"));
        executor.scheduleAtFixedRate(this::updateTemperature, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void updateTemperature() {
        desktopSwingHardware.setTemp(temperatureSimulator.getBeerTemperature());
    }

    @Override
    public void setHeating(boolean on) {
        temperatureSimulator.setHeating(on);
        desktopSwingHardware.setHeating(on);
    }

    @Override
    public float getTemp() {
        return temperatureSimulator.getBeerTemperature();
    }

    @Override
    public void display(String firstRow, String secondRow) {
        desktopSwingHardware.setLcdText(firstRow, secondRow);
    }

    @Override
    public void registerButtonListener(ButtonEnum button, ButtonStateListener listener) {
        desktopSwingHardware.registerButtonListener(button, listener);
    }
}
