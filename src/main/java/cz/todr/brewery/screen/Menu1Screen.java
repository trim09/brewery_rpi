package cz.todr.brewery.screen;

import cz.todr.brewery.hardware.api.ButtonEnum;
import org.springframework.stereotype.Component;

@Component
public class Menu1Screen implements Screen {

    private ScreenExitEvent onExitCallback;

    @Override
    public String getText() {
        return "hello1";
    }

    @Override
    public void onKeyPressed(ButtonEnum button) {
        onExitCallback.exit(null);
    }

    @Override
    public void onEnter(ScreenExitEvent onExitCallback) {
        this.onExitCallback = onExitCallback;
    }
}
