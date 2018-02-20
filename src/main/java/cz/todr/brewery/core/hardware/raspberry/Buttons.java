package cz.todr.brewery.core.hardware.raspberry;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Buttons {

    private static final Logger LOG = LoggerFactory.getLogger(Buttons.class);

    public interface ButtonStateListener {
        void stateChanged(ButtonEnum button, boolean pressed);
    }

    public enum ButtonEnum {
        UP(RaspiPin.GPIO_01),
        DOWN(RaspiPin.GPIO_02),
        LEFT(RaspiPin.GPIO_03),
        RIGHT(RaspiPin.GPIO_04);

        private final Pin pin;

        ButtonEnum(Pin pin) {
            this.pin = pin;
        }

        public Pin getPin() {
            return pin;
        }

        public static Optional<ButtonEnum> findByAddress(int address) {
            for (ButtonEnum button : ButtonEnum.values()) {
                if (button.getPin().getAddress() == address) {
                    return Optional.of(button);
                }
            }
            return Optional.empty();
        }
    }

    private final Map<ButtonEnum, List<ButtonStateListener>> listeners = new HashMap<>();


    public Buttons() {
        if (RaspberryInfo.isRunningOnRaspberryPi()) {
            for (ButtonEnum button : ButtonEnum.values()) {
                initListener(button);
            }
        } else {
            LOG.error("Could not init buttons. Are you running it on Raspberry Pi?");
            consoleFallback();
        }
    }

    private void consoleFallback() {
        Runnable runnable = () -> {
            while(!Thread.interrupted()) {
                try {
                    int read = System.in.read();
                    switch (read) {
                        case 'a':
                            dispatch(ButtonEnum.LEFT, true);
                            break;
                        case 'd':
                            dispatch(ButtonEnum.RIGHT, true);
                            break;
                        case 'w':
                            dispatch(ButtonEnum.UP, true);
                            break;
                        case 's':
                            dispatch(ButtonEnum.DOWN, true);
                            break;
                    }
                } catch (IOException e) {
                    LOG.info("Console read exception", e);
                }
            }
        };
        new Thread(runnable, "ConsoleListener").start();
    }

    private void initListener(ButtonEnum button) {
        GpioFactory.getInstance()
                .provisionDigitalInputPin(button.getPin(), button.name(), PinPullResistance.PULL_UP)
                .addListener((GpioPinListenerDigital) this::processStateChange);
    }

    private void processStateChange(GpioPinDigitalStateChangeEvent event) {
        ButtonEnum.findByAddress(event.getPin().getPin().getAddress())
                .ifPresent(button -> dispatch(button, event.getState().isLow()));
    }

    public void registerListener(ButtonEnum button, ButtonStateListener listener) {
        Objects.requireNonNull(button);
        Objects.requireNonNull(listener);

        List<ButtonStateListener> buttonStateListeners = listeners.computeIfAbsent(button, b -> new ArrayList<>());
        buttonStateListeners.add(listener);
    }

    private void dispatch(ButtonEnum button, boolean pressed) {
        SingleThreadedExecutor.getExecutor().execute(() -> {
            for (ButtonStateListener buttonListener : listeners.getOrDefault(button, Collections.emptyList())) {
                buttonListener.stateChanged(button, pressed);
            }
        });
    }
}
