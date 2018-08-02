package cz.todr.brewery.core.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Utils {
	
	private static final NumberFormat formatter = new DecimalFormat("#0.00");
	
	public static String formatFloat(Float number) {
		if (number == null) {
			return "null";
		}
		return formatter.format(number);
	}
}
