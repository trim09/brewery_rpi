package cz.todr.brewery.core.regulator;

import javax.annotation.Nonnull;

public interface Regulator<T, U> {
	
	@Nonnull
	U controll(@Nonnull T measured, @Nonnull T desiredValue);

}
