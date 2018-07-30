package cz.todr.brewery.screen;

import cz.todr.brewery.hardware.api.ButtonEnum;

public interface Screen {

    String getText();

    void onKeyPressed(ButtonEnum button);

    void onEnter(ScreenExitEvent onExitCallback);
}
