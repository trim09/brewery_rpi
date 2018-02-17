package cz.todr.brewery.core.regulator;

import javax.inject.Named;

@Named
public class TwoStateRegulator implements Regulator<Float, Boolean> {
	
	@Override
	public Boolean controll(Float measured, Float desiredValue) {
		return measured < desiredValue ? true : false;
	}

}
