package cz.todr.brewery.web;

import cz.todr.brewery.core.BreweryCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@RestController
public class BreweryRest {
    private static final Logger LOG = LoggerFactory.getLogger(BreweryRest.class);

    @Autowired
    private BreweryCore breweryCore;

    private static class Status {

        private final long timestamp;
        private final double temp;
        private final double requiredTemp;
        private final boolean heating;

        private Status(double temp, double requiredTemp, boolean heating) {
            this.timestamp = Instant.now().toEpochMilli();
            this.temp = temp;
            this.requiredTemp = requiredTemp;
            this.heating = heating;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getRequiredTemp() {
            return requiredTemp;
        }

        public double getTemp() {
            return temp;
        }

        public boolean isHeating() {
            return heating;
        }
    }

    private static class SetTemp {
        @NotNull
        @Min(-100)
        @Max(200)
        private Float temp;

        public Float getTemp() {
            return temp;
        }

        public void setTemp(Float temp) {
            this.temp = temp;
        }
    }

    @RequestMapping(value = "/status", produces = "application/json")
    public Status getStatus() {
        LOG.debug("Rest reqest: get status");
        return new Status(breweryCore.getTemp(), breweryCore.getRequiredTemp(), breweryCore.isHeating());
    }

    @RequestMapping(value = "/temp", method = RequestMethod.POST, consumes = "application/json")
    public void setStatus(@Valid @RequestBody SetTemp temp) {
        LOG.info("Rest reqest: set temp={}", temp.getTemp());
        breweryCore.setRequiredTemp(temp.getTemp());
    }
}
