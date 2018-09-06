package cz.todr.brewery.core.impl.temperature;

import cz.todr.brewery.core.api.BreweryCore;
import cz.todr.brewery.core.impl.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
public class TemperatureLogger {

	@Autowired
	private BreweryCore breweryCore;

	@Scheduled(fixedRateString = "#{config.periodicLogRate.toMillis()}")
	private void log() {
		try {
			log.info("Temp {} (diff {}°C/min), required temp {}, heating {}",
					Utils.formatFloat(breweryCore.getTemp()),
					Utils.formatFloat(breweryCore.getTemperatureChangeRate()),
					Utils.formatFloat(breweryCore.getRequiredTemp()),
					breweryCore.isHeating() ? "ON" : "OFF");
		} catch (RuntimeException e) {
			log.error("Exception during periodic logging", e);
		}
	}
}
