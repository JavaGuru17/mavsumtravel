package uz.mavsumtravel.mavsumtravel.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String m) {
        super(m + " not found");
    }
}
