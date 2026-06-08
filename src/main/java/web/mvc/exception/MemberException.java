package web.mvc.exception;

import org.springframework.http.HttpStatus;

public class MemberException extends RuntimeException {

    private final HttpStatus httpStatus;

    public MemberException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public MemberException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
