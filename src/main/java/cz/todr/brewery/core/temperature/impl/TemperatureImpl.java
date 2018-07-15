package cz.todr.brewery.core.temperature.impl;

import cz.todr.brewery.core.conf.Config;
import cz.todr.brewery.core.heating.api.Heating;
import cz.todr.brewery.core.temperature.api.Temperature;
import cz.todr.brewery.hardware.api.Hardware;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TemperatureImpl implements Temperature {

    @Autowired
    private Hardware hardware;

    @Autowired
    private TemperatureChangeRate temperatureChangeRate;

    @Autowired
    private Heating heating;

    @Autowired
    private Config config;

    private float requestedTemperature;

    @Override
    public float getRequestedTemperature() {
        return requestedTemperature;
    }

    @Override
    public void requestTemperature(float temperature) {
        requestedTemperature = temperature;
    }

    @Scheduled(fixedRate = 1000)
    private void update() {
        val heatingOn = hardware.getTemp() < requestedTemperature;
        val changeRateExceeded = temperatureChangeRate.getTemperatureChangeRate() > config.getMaximumHeatingSpeed();
        heating.requestHeatingState(!changeRateExceeded && heatingOn);
    }
}
