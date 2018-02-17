package cz.todr.brewery.core;

import org.apache.commons.cli.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class Cli {
	
	public static class CmdArgs {
		private final float requiredTemp;
		private final Float maxHeatingRate;
		private final boolean ignoreHeatingSpeed;
		
		public CmdArgs(float requiredTemp, @Nullable Float maxHeatingRate, boolean ignoreHeatingSpeed) {
			this.requiredTemp = requiredTemp;
			this.maxHeatingRate = maxHeatingRate;
			this.ignoreHeatingSpeed = ignoreHeatingSpeed;
		}
		
		public float getRequiredTemp() {
			return requiredTemp;
		}
		
		@Nullable 
		public Float getMaxHeatingRate() {
			return maxHeatingRate;
		}

		public boolean isIgnoreHeatingSpeed() {
			return ignoreHeatingSpeed;
		}

		@Override
		public String toString() {
			return "CmdArgs [requiredTemp=" + requiredTemp + ", maxHeatingRate=" + maxHeatingRate
					+ ", ignoreHeatingSpeed=" + ignoreHeatingSpeed + "]";
		}
	}
	
	@Nonnull
	private static Options build() {
		Option help = Option.builder("h")
				.longOpt("help")
				.desc("Prints this help")
				.build();
		
		
		Option temp = Option.builder("t")
				.argName("temp")
				.longOpt("temp")
				.hasArg()
				.desc("Set required temperature to given <temp>")
				.build();
		
		Option ignoreHeatingRate = Option.builder("r")
				.argName("diff")
				.hasArg()
				.longOpt("maxHeatingRate")
				.desc("Limit the heating to at most <diff> °C/min")
				.build();
		
        Options options = new Options();
        options.addOption(help);
        options.addOption(temp);
        options.addOption(ignoreHeatingRate);
        
        return options;
	}
	
	@Nonnull
	private static CommandLine parse(Options options, String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
	}
	
	@Nonnull
	private static CmdArgs process(CommandLine cmd) {
		float requiredTemp = cmd.hasOption("t") ? Float.parseFloat(cmd.getOptionValue("t")) : 0f;
		Float maxHeatingRate = cmd.hasOption("r") ? Float.parseFloat(cmd.getOptionValue("r")) : null;
		return new CmdArgs(requiredTemp, maxHeatingRate, maxHeatingRate == null ? true : false);
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(jarFileName(), "", options, getVersionString(), true);
	}
	
	private static String getVersionString() {
		Package pkg = Cli.class.getPackage();
		String title = pkg.getImplementationTitle() == null  ? "unknown" : pkg.getImplementationTitle();
		String version = pkg.getImplementationVersion() == null ? "unknown" : pkg.getImplementationVersion();
		return  String.format("Project %s, version %s.", title, version);		
	}
	
	private static String jarFileName() {
		return new File(Cli.class.getProtectionDomain()
				  .getCodeSource()
				  .getLocation()
				  .getPath())
				.getName();
	}
	
	public static CmdArgs getProcessedArguments(String[] args) {
		Options options = build();
		
		try {
		    CommandLine cmd = parse(options, args);
		    if (cmd.hasOption("h")) {
		    	printHelp(options);
		    	System.exit(-1);
	            throw new IllegalStateException("Wrong usage, please check arguments");

		    }
		    return process(cmd);
		} catch (Exception e) {
            printHelp(options);
            System.err.println("\nError: " + e.getMessage());
            System.exit(-1);
            throw new IllegalStateException("Wrong usage, please check arguments", e);
		}
	}

}
