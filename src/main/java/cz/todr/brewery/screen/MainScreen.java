package cz.todr.brewery.screen;

import cz.todr.brewery.core.BreweryCoreImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
public class MainScreen implements Screen{
    @Autowired
    private BreweryCoreImpl core;

    private final Instant startTime = Instant.now();

    @Override
    public String getText() {
        val isHeating = core.isHeating();
        val temp = core.getTemp();
        val required = core.getRequiredTemp();

        val timeDiff = Duration.between(startTime, Instant.now());

        val line0 = String.format("Temp%6.2f   %s", temp, isHeating ? "ON" : "OFF");
        val line1 = String.format("Req%7.2f %5s", required, durationToString(timeDiff));

        return line0 + "\n" + line1;
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
}
