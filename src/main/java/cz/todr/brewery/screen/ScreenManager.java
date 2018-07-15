package cz.todr.brewery.screen;

import cz.todr.brewery.hardware.api.Hardware;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ScreenManager {

    @Autowired
    private MainScreen mainScreen;

    @Autowired
    private Hardware hw;

    @Scheduled(initialDelay=1000, fixedRate=1000)
    public void updateDisplay () {
        val text = mainScreen.getText();
        val lines = text.split("\n");
        hw.display(lines[0], lines[1]);
    }

}
