package cz.todr.brewery.core.regulator;

import org.springframework.stereotype.Controller;

@Controller
public class TwoStateRegulator implements Regulator<Float, Boolean> {
	
	@Override
	public Boolean controll(Float measured, Float desiredValue) {
		return measured < desiredValue ? true : false;
	}

}
