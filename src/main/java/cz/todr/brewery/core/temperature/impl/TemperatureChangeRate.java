package cz.todr.brewery.core.temperature.impl;

import cz.todr.brewery.hardware.api.Hardware;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class TemperatureChangeRate {

    @Autowired
    private Hardware hardware;

    private float temperature;

    private float temperatureChangeRate;

    @PostConstruct
    private void init() {
        temperature = hardware.getTemp();
    }

    /** @return number that represents °C/min */
    public float getTemperatureChangeRate() {
        return temperatureChangeRate;
    }

    @Scheduled(initialDelay = 600000, fixedRate=600000)
    private void update() {
        val tempNow = hardware.getTemp();
        temperatureChangeRate = tempNow - temperature;
        log.trace("Calculated heating speed {}°C/min (temp {}@{} -> {}@{})",
                temperatureChangeRate, temperature, tempNow);
        temperature = tempNow;
    }
}
