package cz.todr.brewery.screen;

import com.google.common.collect.ImmutableList;
import cz.todr.brewery.core.impl.BreweryCoreImpl;
import cz.todr.brewery.hardware.api.ButtonEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class AutomaticBrewingScreen implements Screen {
    @Data
    private static class Recipe {
        private final float temp;
        private final Duration duration;
    }

    private static final List<Recipe> recipes = ImmutableList.of(
            new Recipe(52.f, Duration.of(30, ChronoUnit.MINUTES)),
            new Recipe(72.f, Duration.of(30, ChronoUnit.MINUTES)),
            new Recipe(76.f, Duration.of(30, ChronoUnit.MINUTES))
    );

    @Autowired
    private BreweryCoreImpl core;

    private final Instant startTime = Instant.now();

    private ScreenExitEvent onExitCallback;

    private Recipe currentTempTarget;
    private Instant currentTempTargetMetAt;
    private boolean running;

    @Override
    public String getText() {
        checkStateUpdate();


        val isHeating = core.isHeating();
        val temp = core.getTemp();
        val requiredTemp = core.getRequiredTemp();

        val timeDiff = Duration.between(startTime, Instant.now());

        val line0 = String.format("Temp%6.2f   %s", temp, isHeating ? "ON" : "OFF");
        val line1 = String.format("aeq%7.2f %5s", requiredTemp, ScreenUtils.durationToString(timeDiff));

        return line0 + "\n" + line1;
    }

    private Recipe tryFindCurrentTarget() {
        val temp = core.getTemp();
        return recipes.stream()
                .filter(r -> r.getTemp() >= temp)
                .findFirst()
                .orElse(null);
    }

    private Recipe findNext(Recipe prev) {
        if (prev == null) {
            return null;
        }

        return recipes.stream()
                .filter(r -> prev.getTemp() < r.getTemp())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void onKeyPressed(ButtonEnum button) {
        running = false;
        onExitCallback.exit(null);
    }

    @Scheduled(fixedRate = 1000)
    private void stateUpdater() {
        checkStateUpdate();
    }

    private void checkStateUpdate() {
        if (running) {
            evaluateTemperatureMet();
            evaluateRecipeDone();
            evaluateBrewingDone();
        }
    }

    private void evaluateTemperatureMet() {
        if (currentTempTargetMetAt == null && currentTempTarget != null) {
            val temp = core.getTemp();
            if (temp >= currentTempTarget.getTemp()) {
                currentTempTargetMetAt = Instant.now();
            }
        }
    }

    private void evaluateRecipeDone() {
        if (currentTempTarget != null && currentTempTargetMetAt != null
                && Instant.now().isBefore(currentTempTargetMetAt.plus(currentTempTarget.getDuration()))) {
            currentTempTarget = findNext(currentTempTarget);
        }
    }

    private void evaluateBrewingDone() {
        if (currentTempTarget == null) {
            running = false;
            onExitCallback.exit(null);
        }
    }

    @Override
    public void onEnter(ScreenExitEvent onExitCallback) {
        this.onExitCallback = onExitCallback;
        currentTempTarget = tryFindCurrentTarget();
        running = currentTempTarget != null;
    }
}
