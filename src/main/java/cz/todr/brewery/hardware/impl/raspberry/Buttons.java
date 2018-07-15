package cz.todr.brewery.hardware.impl.raspberry;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import cz.todr.brewery.core.utils.SingleThreadedExecutor;
import cz.todr.brewery.hardware.api.ButtonEnum;
import cz.todr.brewery.hardware.api.ButtonStateListener;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Buttons {

    private final Map<ButtonEnum, List<ButtonStateListener>> listeners = new HashMap<>();


    public Buttons() {
        for (ButtonEnum button : ButtonEnum.values()) {
            initListener(button);
        }
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
