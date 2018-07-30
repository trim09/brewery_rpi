package cz.todr.brewery.screen;

import com.google.common.collect.ImmutableMap;
import cz.todr.brewery.hardware.api.ButtonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MenuScreen implements Screen {

    private static final String MENU = "Menu1\nMenu2";

    private static final int MENU_LENGTH = MENU.split("\n").length;

    private int cursorPosition;

    private ScreenExitEvent onExitCallback;

    private Map<ButtonEnum, Runnable> actions = ImmutableMap.of(
                ButtonEnum.UP, () -> cursorPosition = Math.max(0, cursorPosition - 1),
                ButtonEnum.MID, () -> enterMenu(cursorPosition),
                ButtonEnum.DOWN, () -> cursorPosition = Math.min(MENU_LENGTH - 1, cursorPosition + 1)
            );

    private void enterMenu(int index) {
        if (index == 0)
            onExitCallback.exit(Menu1Screen.class);
        else
            onExitCallback.exit(Menu2Screen.class);
    }

    @Override
    public String getText() {
        return MENU;
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
