package web.mvc.exception;

import org.springframework.http.HttpStatus;

public class PlaceException extends RuntimeException {

    private final HttpStatus httpStatus;

    public PlaceException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public PlaceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
