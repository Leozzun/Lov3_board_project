package web.mvc.exception;

import org.springframework.http.HttpStatus;

public class BoardException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BoardException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BoardException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
