package cz.todr.brewery.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.todr.brewery.hardware.api.ButtonEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MenuScreen implements Screen {
    @Data
    private static class MenuItem {
        private final String text;
        private final Class<? extends Screen> nextScreen;
    }

    private List<MenuItem> menu = ImmutableList.of(
            new MenuItem("Measure temp", MeasureTempScreen.class),
            new MenuItem("Regulate temp", RegulateTempScreen.class),
            new MenuItem("Automatic brewing", AutomaticBrewingScreen.class)
    );

    private int cursorPosition;

    private ScreenExitEvent onExitCallback;

    private Map<ButtonEnum, Runnable> actions = ImmutableMap.of(
                ButtonEnum.UP, () -> cursorPosition = Math.max(0, cursorPosition - 1),
                ButtonEnum.MID, () -> enterMenu(cursorPosition),
                ButtonEnum.DOWN, () -> cursorPosition = Math.min(menu.size() - 1, cursorPosition + 1)
            );

    private void enterMenu(int index) {
        onExitCallback.exit(menu.get(index).getNextScreen());
    }

    @Override
    public String getText() {
        return menu.stream()
                .skip(cursorPosition)
                .map(MenuItem::getText)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void onKeyPressed(ButtonEnum button) {
        actions.getOrDefault(button, () -> {}).run();
    }

    @Override
    public void onEnter(ScreenExitEvent onExitCallback) {
        this.onExitCallback = onExitCallback;
        cursorPosition = 0;
    }
}
