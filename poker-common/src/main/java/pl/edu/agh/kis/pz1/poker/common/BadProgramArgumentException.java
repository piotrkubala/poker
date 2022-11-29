package pl.edu.agh.kis.pz1.poker.common;

/**
 * Exception thrown when the program is started with wrong arguments.
 */
public class BadProgramArgumentException extends RuntimeException {
    public BadProgramArgumentException(String message) {
        super(message);
    }
}

