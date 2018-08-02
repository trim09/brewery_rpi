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
public class RegulateTempScreen implements Screen{
    @Autowired
    private BreweryCoreImpl core;

    private final Instant startTime = Instant.now();

    private ScreenExitEvent onExitCallback;

    @Override
    public String getText() {
        val isHeating = core.isHeating();
        val temp = core.getTemp();
        val requiredTemp = core.getRequiredTemp();

        val timeDiff = Duration.between(startTime, Instant.now());

        val line0 = String.format("Temp%6.2f   %s", temp, isHeating ? "ON" : "OFF");
        val line1 = String.format("Req%7.2f %5s", requiredTemp, ScreenUtils.durationToString(timeDiff));

        return line0 + "\n" + line1;
    }

    @Override
    public void onKeyPressed(ButtonEnum button) {
        onExitCallback.exit(null);
    }

    @Override
    public void onEnter(ScreenExitEvent onExitCallback) {
        if (core.getRequiredTemp() == null) {
            core.setRequiredTemp(0.F);
        }
        this.onExitCallback = onExitCallback;
    }
}
