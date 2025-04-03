package uz.mavsumtravel.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String m) {
        super(m + " already exists");
    }
}
