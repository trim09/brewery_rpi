package cz.todr.brewery.core.temperature.api;

public interface Temperature {

    Float getRequestedTemperature();

    void requestTemperature(Float temperature);
}
