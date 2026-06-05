package web.mvc.exception;

import org.springframework.http.HttpStatus;

/**
 * 회원 관련 예외
 * 서비스에서 throw new MemberException("메시지") 형태로 사용
 */
public class MemberException extends RuntimeException {

    private final HttpStatus httpStatus;

    public MemberException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;   // 기본: 400
    }

    public MemberException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
