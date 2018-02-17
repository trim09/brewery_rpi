package cz.todr.brewery.core.hardware.raspberry;

import com.pi4j.component.lcd.LCDBase;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class FallbackDisplay extends LCDBase {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackDisplay.class);

    private static final int PRINT_PERIOD = 1;

    private String screen = String.format("%32s", "");
    private int cursorIndex;

    public FallbackDisplay() {
        ScheduledExecutorService executor = SingleThreadedExecutor.getExecutor();
        executor.scheduleAtFixedRate(this::printScreenToLog, PRINT_PERIOD, PRINT_PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount() {
        return 16;
    }

    private int size() {
        return getRowCount() * getColumnCount();
    }

    @Override
    public void setCursorPosition(int row, int column) {
        if (row > getRowCount() || column > getColumnCount()) {
            throw new IllegalArgumentException();
        }

        cursorIndex = row * getColumnCount() +  column;
    }

    @Override
    public void write(byte data) {
        throw new UnsupportedOperationException();
    }

    private void printScreenToLog() {
        LOG.info("LCD text: '{}    {}'", screen.substring(0, 16), screen.substring(16));
    }

    @Override
    public void write(String data) {
        if (cursorIndex + data.length() > size()) {
            data = data.substring(0, size() - cursorIndex);
        }
        screen = screen.substring(0, cursorIndex) + data + screen.substring(cursorIndex + data.length());
    }

}
