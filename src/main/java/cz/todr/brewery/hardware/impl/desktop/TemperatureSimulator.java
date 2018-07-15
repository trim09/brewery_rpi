package cz.todr.brewery.hardware.impl.desktop;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TemperatureSimulator {

    private static final float ENVIRONMENT_TEMPERATURE = 10;
    private static final float MAX_TEMPERATURE = 110;

    @Getter
    private float beerTemperature = 20;

    @Getter
    @Setter
    private boolean heating = false;

    public TemperatureSimulator() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
                runnable -> new Thread(runnable, "TemperatureSimulator"));
        executor.scheduleAtFixedRate(this::tempCorrection, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void tempCorrection() {
        float newTemp = heating ? beerTemperature + 1 : beerTemperature - 1;
        newTemp = Math.min(newTemp, MAX_TEMPERATURE);
        newTemp = Math.max(newTemp, ENVIRONMENT_TEMPERATURE);
        beerTemperature = newTemp;
    }
}
