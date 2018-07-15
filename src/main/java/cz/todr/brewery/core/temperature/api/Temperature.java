package cz.todr.brewery.core.temperature.api;

public interface Temperature {

    float getRequestedTemperature();

    void requestTemperature(float temperature);
}
