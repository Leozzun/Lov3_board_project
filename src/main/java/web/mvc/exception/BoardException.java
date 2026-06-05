package web.mvc.exception;

import org.springframework.http.HttpStatus;

/**
 * 게시글 관련 예외
 */
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

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
