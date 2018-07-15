package cz.todr.brewery;

public class NotOnRaspberryException extends BreweryException {
    public NotOnRaspberryException() {
        super("Could not init buttons. Are you running it on Raspberry Pi?");
    }
}
