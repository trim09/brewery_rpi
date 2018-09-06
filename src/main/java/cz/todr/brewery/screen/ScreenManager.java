package cz.todr.brewery.screen;

import cz.todr.brewery.core.api.BreweryCore;
import cz.todr.brewery.hardware.api.ButtonEnum;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@Slf4j
public class ScreenManager {

    @Autowired
    private BreweryCore breweryCore;

    @Autowired
    private List<Screen> screens;

    @Autowired
    private RegulateTempScreen initScreen;

    @Autowired
    private MenuScreen defaultScreen;

    private Screen current;

    @PostConstruct
    private void init() {
        current = initScreen;
        current.onEnter(this::exit);
        breweryCore.registerButtonListener(this::buttonsListener);
    }

    private void exit(Class<? extends Screen> next) {
        val oldScreen = current;
        if (next == null) {
            current = defaultScreen;
        } else {
            current = screens.stream().filter(s -> s.getClass() == next).findAny().orElse(defaultScreen);
        }

        log.info("Screen transition: {} -> {} ({})", getScreenName(oldScreen), getScreenName(current), next);

        current.onEnter(this::exit);
    }

    private String getScreenName(Screen screen) {
        return screen.getClass().getSimpleName();
    }

    private void buttonsListener(ButtonEnum button) {
        log.debug("KeyEvent {}", button);
        current.onKeyPressed(button);
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void updateDisplay() {
        breweryCore.display(current.getText());
    }
}
