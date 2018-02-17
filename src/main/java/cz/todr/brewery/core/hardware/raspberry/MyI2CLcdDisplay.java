package cz.todr.brewery.core.hardware.raspberry;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.I2CLcdDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyI2CLcdDisplay implements LCD {

    private static final Logger LOG = LoggerFactory.getLogger(MyI2CLcdDisplay.class);

    private LCD lcd;

    public MyI2CLcdDisplay() throws Exception {
        try {
            lcd = new I2CLcdDisplay(2, 16, 1, 39, 3, 0, 1, 2, 7, 6, 5, 4);
        } catch (UnsatisfiedLinkError e) {
            LOG.error("Could not init LCD display. Are you running it on Raspberry Pi?", e);
            lcd = new LCDBase() {
                @Override
                public int getRowCount() {
                    return 2;
                }

                @Override
                public int getColumnCount() {
                    return 16;
                }

                @Override
                public void setCursorPosition(int row, int column) {

                }

                @Override
                public void write(byte data) {
                    LOG.info("LCD text: '{}'", (char)data);
                }

                @Override
                public void write(String data) {
                    LOG.info("LCD text: '{}'", data.substring(0, getColumnCount()));
                }
            };
        }
    }

    @Override
    public int getRowCount() {
        return lcd.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return lcd.getColumnCount();
    }

    @Override
    public void clear() {
        lcd.clear();
    }

    @Override
    public void clear(int row) {
        lcd.clear(row);
    }

    @Override
    public void clear(int row, int column, int length) {
        lcd.clear(row, column, length);
    }

    @Override
    public void setCursorHome() {
        lcd.setCursorHome();
    }

    @Override
    public void setCursorPosition(int row) {
        lcd.setCursorPosition(row);
    }

    @Override
    public void setCursorPosition(int row, int column) {
        lcd.setCursorPosition(row, column);
    }

    @Override
    public void write(String data) {
        lcd.write(data);
    }

    @Override
    public void write(String data, Object... arguments) {
        lcd.write(data, arguments);
    }

    @Override
    public void write(char[] data) {
        lcd.write(data);
    }

    @Override
    public void write(byte[] data) {
        lcd.write(data);
    }

    @Override
    public void write(char data) {
        lcd.write(data);
    }

    @Override
    public void write(byte data) {
        lcd.write(data);
    }

    @Override
    public void write(int row, String data, LCDTextAlignment alignment) {
        lcd.write(row, data, alignment);
    }

    @Override
    public void write(int row, String data, LCDTextAlignment alignment, Object... arguments) {
        lcd.write(row, data, alignment, arguments);
    }

    @Override
    public void write(int row, String data) {
        lcd.write(row, data);
    }

    @Override
    public void write(int row, String data, Object... arguments) {
        lcd.write(row, data, arguments);
    }

    @Override
    public void write(int row, char[] data) {
        lcd.write(row, data);
    }

    @Override
    public void write(int row, byte[] data) {
        lcd.write(row, data);
    }

    @Override
    public void write(int row, char data) {
        lcd.write(row, data);
    }

    @Override
    public void write(int row, byte data) {
        lcd.write(row, data);
    }

    @Override
    public void write(int row, int column, String data) {
        lcd.write(row, column, data);
    }

    @Override
    public void write(int row, int column, String data, Object... arguments) {
        lcd.write(row, column, data, arguments);
    }

    @Override
    public void write(int row, int column, char[] data) {
        lcd.write(row, column, data);
    }

    @Override
    public void write(int row, int column, byte[] data) {
        lcd.write(row, column, data);
    }

    @Override
    public void write(int row, int column, char data) {
        lcd.write(row, column, data);
    }

    @Override
    public void write(int row, int column, byte data) {
        lcd.write(row, column, data);
    }

    @Override
    public void writeln(int row, String data) {
        lcd.writeln(row, data);
    }

    @Override
    public void writeln(int row, String data, Object... arguments) {
        lcd.writeln(row, data, arguments);
    }

    @Override
    public void writeln(int row, String data, LCDTextAlignment alignment) {
        lcd.writeln(row, data, alignment);
    }

    @Override
    public void writeln(int row, String data, LCDTextAlignment alignment, Object... arguments) {
        lcd.writeln(row, data, alignment, arguments);
    }

    @Override
    public void setName(String name) {
        lcd.setName(name);
    }

    @Override
    public String getName() {
        return lcd.getName();
    }

    @Override
    public void setTag(Object tag) {
        lcd.setTag(tag);
    }

    @Override
    public Object getTag() {
        return lcd.getTag();
    }

    @Override
    public void setProperty(String key, String value) {
        lcd.setProperty(key, value);
    }

    @Override
    public boolean hasProperty(String key) {
        return lcd.hasProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return lcd.getProperty(key, defaultValue);
    }

    @Override
    public String getProperty(String key) {
        return lcd.getProperty(key);
    }

    @Override
    public Map<String, String> getProperties() {
        return lcd.getProperties();
    }

    @Override
    public void removeProperty(String key) {
        lcd.removeProperty(key);
    }

    @Override
    public void clearProperties() {
        lcd.clearProperties();
    }
}
