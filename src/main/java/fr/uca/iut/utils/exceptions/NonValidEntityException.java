package fr.uca.iut.utils.exceptions;

public class NonValidEntityException extends RuntimeException {
    public NonValidEntityException(String message) {
        super(message);
    }
}