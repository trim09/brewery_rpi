package cz.todr.brewery.core.impl.temperature.impl;

import cz.todr.brewery.conf.Config;
import cz.todr.brewery.core.impl.heating.api.Heating;
import cz.todr.brewery.core.impl.temperature.api.Temperature;
import cz.todr.brewery.core.impl.temperature.api.TemperatureChangeRate;
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

    private Float requestedTemperature;

    @Override
    public Float getRequestedTemperature() {
        return requestedTemperature;
    }

    @Override
    public void requestTemperature(Float temperature) {
        requestedTemperature = temperature;
    }

    @Scheduled(fixedRate = 1000)
    private void update() {
        val heatingOn = requestedTemperature != null && (hardware.getTemp() < requestedTemperature);
        val changeRateExceeded = temperatureChangeRate.getTemperatureChangeRate() > config.getMaximumHeatingSpeed();
        heating.setHeating(!changeRateExceeded && heatingOn);
    }
}
