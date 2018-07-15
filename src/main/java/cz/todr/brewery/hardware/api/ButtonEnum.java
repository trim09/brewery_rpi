package cz.todr.brewery.hardware.api;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum ButtonEnum {
    UP(RaspiPin.GPIO_01),
    DOWN(RaspiPin.GPIO_02),
    MID(RaspiPin.GPIO_03);

    private final Pin pin;

    public static Optional<ButtonEnum> findByAddress(int address) {
        return Arrays.stream(values())
                .filter(pin -> pin.getPin().getAddress() == address)
                .findAny();
    }
}
