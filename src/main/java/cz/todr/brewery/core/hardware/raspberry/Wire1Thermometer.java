package cz.todr.brewery.core.hardware.raspberry;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class Wire1Thermometer {
	private static final Logger LOG = LoggerFactory.getLogger(Wire1Thermometer.class);

	private final class SlaveDetail {
        private final Path path;
        private volatile float temp;

        SlaveDetail(Path path) {
            this.path = path;
        }
    }

    private final static Path W1_DEVICES_PATH = Paths.get("/sys/bus/w1/devices/");
    private final static String W1_SLAVE = "w1_slave";

    private final Map<String, SlaveDetail> slaves;
    private final RateLimiter rateLimiter;

    public Wire1Thermometer() {
        this(getAllDevices());
    }

    private Wire1Thermometer(Collection<String> devices) {
        this.slaves = devices.stream().collect(Collectors.toMap(
                Function.identity(),
                device -> new SlaveDetail(getSlaveDataPath(device))
        ));
        this.rateLimiter = RateLimiter.create(1.0/60);

        startPoolingTemp();
    }

    private static Collection<String> getAllDevices() {
    	if (RaspberryInfo.isRunningOnRaspberryPi()) {
			try {
				if (!Files.exists(W1_DEVICES_PATH)) {
					LOG.error("Path not Found {}", W1_DEVICES_PATH);
					return Collections.emptyList();
				}
				
			    return Files.walk(W1_DEVICES_PATH, 2, FileVisitOption.FOLLOW_LINKS)
			            .filter(Files::isRegularFile)
			            .filter(p -> "w1_master_slaves".equals(p.getFileName().toString()))
			            .flatMap(p -> {
			                try {
			                    return Files.lines(p);
			                } catch (IOException e) {
			                    throw new UncheckedIOException(e);
			                }
			            })
			            .filter(slave -> Files.isRegularFile(getSlaveDataPath(slave)))
			            .collect(Collectors.toSet());
			} catch (IOException e) {
			    throw new UncheckedIOException(e);
			}
    	} else {
    		LOG.error("Cannot get all devices. Is it running on RaspberryPi board? Is the thermometer connected? Check {}", W1_DEVICES_PATH);
    		return Collections.emptyList();
    	}
    }

    private static Path getSlaveDataPath(String slave) {
        return W1_DEVICES_PATH.resolve(slave).resolve(W1_SLAVE);
    }

    private void startPoolingTemp() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
        		runnable -> new Thread(runnable, "wire1-bus"));
        executor.scheduleAtFixedRate(this::poolAllTemp, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void poolAllTemp() {
    	try {
	        for (SlaveDetail slaveDetail : slaves.values()) {
	            slaveDetail.temp = readTempFromPath(slaveDetail.path);
	        }
    	} catch (RuntimeException e) {
    		LOG.error("Exception during temperature history autopdate", e);
    	}
    }

    private float readTempFromPath(Path dataFile) {
        try {
            List<String> lines = Files.readAllLines(dataFile);
            for (String line : lines) {
                int startPos = line.indexOf("t=");
                if (startPos >= 0) {
                    String strTemp = line.substring(startPos + 2, line.length());
                    return Float.parseFloat(strTemp) / 1000;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return Float.NaN;
    }

    public float getTempSingleDevice() {
    	if (slaves.size() != 1) {
            if (rateLimiter.tryAcquire()) {
                LOG.error("Could not find thermometer. Is the thermometer connected? Check {}", W1_DEVICES_PATH);
            }
            //throw new IllegalStateException("Number of Wire1 slaves " + slaves.size());
    		return 0f; 
        }

        return slaves.values().iterator().next().temp;
    }

}
