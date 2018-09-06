package cz.todr.brewery.core.impl.temperature.api;

public interface Temperature {

    Float getRequestedTemperature();

    void requestTemperature(Float temperature);
}
