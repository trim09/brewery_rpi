package cz.todr.brewery.screen;

import java.time.Duration;

public class ScreenUtils {

    public static String durationToString(Duration dur) {
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
