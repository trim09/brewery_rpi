package cz.todr.brewery.hardware.impl.raspberry;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.impl.I2CLcdDisplay;
import cz.todr.brewery.NotOnRaspberryException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Display {

    public static LCD getDisplay() {
        try {
            return new I2CLcdDisplay(2, 16, 1, 39, 3, 0, 1, 2, 7, 6, 5, 4);
        } catch (Exception e) {
            throw new NotOnRaspberryException();
        }
    }
}
