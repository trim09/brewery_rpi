package cz.todr.brewery.screen;

public interface ScreenExitEvent {
    void exit(Class<? extends Screen> next);
}
