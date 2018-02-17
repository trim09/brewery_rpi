package cz.todr.brewery.lcd;

import cz.todr.brewery.core.BreweryCore;
import cz.todr.brewery.core.hardware.raspberry.MyI2CLcdDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Named
public class LcdMonitor {
    private static final Logger LOG = LoggerFactory.getLogger(LcdMonitor.class);

    @Inject
    private BreweryCore core;

    private MyI2CLcdDisplay lcd;

    private final Instant startTime = Instant.now();

    @PostConstruct
    private void init() {
        try {
            lcd = new MyI2CLcdDisplay();
        } catch (Exception e) {
            LOG.error("Could not initialise LCD display");
        }
    }

    @Scheduled(initialDelay=1000, fixedRate=1000)
    public void updateDisplay () {
        LOG.debug("Starting display update");

        boolean isHeating = core.isHeating();
        float temp = core.getTemp();
        float required = core.getRequiredTemp();

        Duration timeDiff = Duration.between(startTime, Instant.now());

        String line0 = String.format("Temp%6.2f   %s", temp, isHeating ? "ON" : "OFF");
        lcd.writeln(0, line0);
        String line1 = String.format("Req%7.2f %5s", required, durationToString(timeDiff));
        lcd.writeln(1, line1);

        LOG.debug("End display update");
    }

    private String durationToString(Duration dur) {
        if (dur.toDays() >= 99) {
            return String.format("%dd", dur.toDays());
        } else if (dur.toHours() > 99) {
            return String.format("%dd%02d", dur.toDays(), dur.minusDays(dur.toDays()).toHours());
        } else  if (dur.toMinutes() > 99) {
            return String.format("%dh%02d", dur.toHours(), dur.minusHours(dur.toHours()).toMinutes());
        } else  {
            return String.format("%dm%02d", dur.toMinutes(), dur.minusMinutes(dur.toMinutes()).getSeconds());
        }
    }

//    public static void main(String[] args) {
//
//        boolean isHeating = true;
//        float temp = 2.133349f;
//        float required = 0f;
//
//        Instant start = Instant.now().minus(5, ChronoUnit.MINUTES).minus(6, ChronoUnit.SECONDS);
//        Instant end = Instant.now();
//        Duration dur = Duration.between(start, end);
//
//        String line0 = String.format("Temp%6.2f   %s", temp, isHeating ? "ON" : "OFF");
//        String line1 = String.format("Req%7.2f %2d:%02d", required, dur.toMinutes(), dur.minusMinutes(dur.toMinutes()).getSeconds());
//
//        DecimalFormat format = new DecimalFormat("0.00");
//
//        String line2 = "1234567890123456";
//
//        System.out.println(line0);
//        System.out.println(line1);
//        System.out.println(line2);
//    }

}
