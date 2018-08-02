package cz.todr.brewery.screen;

import cz.todr.brewery.core.BreweryCoreImpl;
import cz.todr.brewery.hardware.api.ButtonEnum;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class MeasureTempScreen implements Screen{
    @Autowired
    private BreweryCoreImpl core;

    private final Instant startTime = Instant.now();

    private ScreenExitEvent onExitCallback;

    @Override
    public String getText() {
        val temp = core.getTemp();
        val timeDiff = Duration.between(startTime, Instant.now());

        return String.format("Temp%6.2f %5s", temp, ScreenUtils.durationToString(timeDiff));
    }

    @Override
    public void onKeyPressed(ButtonEnum button) {
        onExitCallback.exit(null);
    }

    @Override
    public void onEnter(ScreenExitEvent onExitCallback) {
        core.setRequiredTemp(null);
        this.onExitCallback = onExitCallback;
    }
}
