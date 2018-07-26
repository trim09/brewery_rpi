package cz.todr.brewery.screen;

import cz.todr.brewery.core.BreweryCore;
import cz.todr.brewery.hardware.api.ButtonEnum;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Slf4j
public class ScreenManager {

    @Autowired
    private MainScreen mainScreen;

    @Autowired
    private BreweryCore breweryCore;

    @PostConstruct
    private void init() {
        breweryCore.registerButtonListener(this::buttonsListener);
    }

    private void buttonsListener(ButtonEnum button) {
        switch(button) {
            case UP:
                breweryCore.setRequiredTemp(breweryCore.getRequiredTemp() + 1);
                break;
            case DOWN:
                breweryCore.setRequiredTemp(breweryCore.getRequiredTemp() - 1);
                break;
            case MID:
                break;
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void updateDisplay() {
        val text = mainScreen.getText();
        breweryCore.display(text);
    }

}
