package web.mvc.exception;

import org.springframework.http.HttpStatus;

public class DateRequestException extends RuntimeException {

    private final HttpStatus httpStatus;

    public DateRequestException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public DateRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
