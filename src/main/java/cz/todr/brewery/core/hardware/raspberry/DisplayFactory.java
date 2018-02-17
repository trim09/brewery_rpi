package cz.todr.brewery.core.hardware.raspberry;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.impl.I2CLcdDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DisplayFactory.class);

    private static LCD lcd;

    public static LCD getDisplay() {
        return lcd;
    }

    static {
        try {
            lcd = new I2CLcdDisplay(2, 16, 1, 39, 3, 0, 1, 2, 7, 6, 5, 4);
        } catch (Exception|UnsatisfiedLinkError e) {
            LOG.error("Could not init LCD display. Are you running it on Raspberry Pi?", e);
            lcd = new FallbackDisplay();
        }
    }

}
