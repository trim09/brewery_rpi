package cz.todr.brewery.core.hardware.raspberry;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import com.pi4j.system.SystemInfo.BoardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

public class RaspberryInfo {
	private static final Logger LOG = LoggerFactory.getLogger(RaspberryInfo.class);
	
	private static BoardType boardType;

    public static void printInfo() {
        if (isRunningOnRaspberryPi()) {
        	try {
	            // display a few of the available system information properties
	            LOG.info("----------------------------------------------------");
	            LOG.info("HARDWARE INFO");
	            LOG.info("----------------------------------------------------");
	            LOG.info("Serial Number     :  " + SystemInfo.getSerial());
	            LOG.info("CPU Revision      :  " + SystemInfo.getCpuRevision());
	            LOG.info("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
	            LOG.info("CPU Part          :  " + SystemInfo.getCpuPart());
	            LOG.info("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
	            LOG.info("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
	            LOG.info("CPU Model Name    :  " + SystemInfo.getModelName());
	            LOG.info("Processor         :  " + SystemInfo.getProcessor());
	            LOG.info("Hardware Revision :  " + SystemInfo.getRevision());
	            LOG.info("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
	            LOG.info("Board Type        :  " + SystemInfo.getBoardType().name());
	
	            LOG.info("----------------------------------------------------");
	            LOG.info("MEMORY INFO");
	            LOG.info("----------------------------------------------------");
	            LOG.info("Total Memory      :  " + SystemInfo.getMemoryTotal());
	            LOG.info("Used Memory       :  " + SystemInfo.getMemoryUsed());
	            LOG.info("Free Memory       :  " + SystemInfo.getMemoryFree());
	            LOG.info("Shared Memory     :  " + SystemInfo.getMemoryShared());
	            LOG.info("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
	            LOG.info("Cached Memory     :  " + SystemInfo.getMemoryCached());
	            LOG.info("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
	            LOG.info("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
	            LOG.info("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());
	
	            LOG.info("----------------------------------------------------");
	            LOG.info("OPERATING SYSTEM INFO");
	            LOG.info("----------------------------------------------------");
	            LOG.info("OS Name           :  " + SystemInfo.getOsName());
	            LOG.info("OS Version        :  " + SystemInfo.getOsVersion());
	            LOG.info("OS Architecture   :  " + SystemInfo.getOsArch());
	            LOG.info("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
	            LOG.info("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());
	
	            LOG.info("----------------------------------------------------");
	            LOG.info("JAVA ENVIRONMENT INFO");
	            LOG.info("----------------------------------------------------");
	            LOG.info("Java Vendor       :  " + SystemInfo.getJavaVendor());
	            LOG.info("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
	            LOG.info("Java Version      :  " + SystemInfo.getJavaVersion());
	            LOG.info("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
	            LOG.info("Java Runtime      :  " + SystemInfo.getJavaRuntime());
	
	            LOG.info("----------------------------------------------------");
	            LOG.info("NETWORK INFO");
	            LOG.info("----------------------------------------------------");
	
	            // display some of the network information
	            LOG.info("Hostname          :  " + NetworkInfo.getHostname());
	            for (String ipAddress : NetworkInfo.getIPAddresses())
	                LOG.info("IP Addresses      :  " + ipAddress);
	            for (String fqdn : NetworkInfo.getFQDNs())
	                LOG.info("FQDN              :  " + fqdn);
	            for (String nameserver : NetworkInfo.getNameservers())
	                LOG.info("Nameserver        :  " + nameserver);
	
	            LOG.info("----------------------------------------------------");
	            LOG.info("CODEC INFO");
	            LOG.info("----------------------------------------------------");
	            LOG.info("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
	            LOG.info("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
	            LOG.info("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());
	
	            LOG.info("----------------------------------------------------");
	            LOG.info("CLOCK INFO");
	            LOG.info("----------------------------------------------------");
	            LOG.info("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
	            LOG.info("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
	            LOG.info("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
	            LOG.info("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
	            LOG.info("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
	            LOG.info("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
	            LOG.info("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
	            LOG.info("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
	            LOG.info("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
	            LOG.info("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
	            LOG.info("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
	            LOG.info("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());
	        } catch (IOException | InterruptedException | ParseException e) {
	            e.printStackTrace();
	        }
        } else {
        	LOG.info("This is not running on RaspberryPi board");
        }
    }
    
    public static boolean isRunningOnRaspberryPi() {
    	if (boardType == null) {
        	try { 
        		boardType = SystemInfo.getBoardType();
        	} catch (Exception e) {
        		LOG.error("Could not identify Raspberry board");
        		boardType = BoardType.UNKNOWN;
        	}	
    	}
 
		return BoardType.UNKNOWN != boardType;
    }
}
