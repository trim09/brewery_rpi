package cz.todr.brewery.hardware.impl;

import cz.todr.brewery.hardware.api.Hardware;
import cz.todr.brewery.hardware.impl.desktop.DesktopHardwareSimulator;
import cz.todr.brewery.hardware.impl.raspberry.RaspberryHardware;
import cz.todr.brewery.hardware.impl.raspberry.RaspberryInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HardwareFactory {

    @Bean
    public Hardware getHardware() {
        if (RaspberryInfo.isRunningOnRaspberryPi()) {
            return new HardwareDecorator(new RaspberryHardware());
        } else {
            return new HardwareDecorator(new DesktopHardwareSimulator());
        }
    }
}
