package web.mvc.exception;

import org.springframework.http.HttpStatus;

/**
 * 댓글 관련 예외
 */
public class CommentException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CommentException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CommentException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
