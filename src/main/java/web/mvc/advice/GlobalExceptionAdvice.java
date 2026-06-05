package web.mvc.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.mvc.exception.BoardException;
import web.mvc.exception.CommentException;
import web.mvc.exception.MemberException;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리기
 * 컨트롤러에서 발생한 예외를 공통으로 처리하여 일관된 에러 응답 반환
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    // 회원 관련 예외 처리
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, String>> handleMemberException(MemberException e) {
        return buildErrorResponse(e.getMessage(), e.getHttpStatus().value());
    }

    // 게시글 관련 예외 처리
    @ExceptionHandler(BoardException.class)
    public ResponseEntity<Map<String, String>> handleBoardException(BoardException e) {
        return buildErrorResponse(e.getMessage(), e.getHttpStatus().value());
    }

    // 댓글 관련 예외 처리
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<Map<String, String>> handleCommentException(CommentException e) {
        return buildErrorResponse(e.getMessage(), e.getHttpStatus().value());
    }

    // TODO: 필요한 예외 핸들러 추가

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, int status) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        body.put("status", String.valueOf(status));
        return ResponseEntity.status(status).body(body);
    }
}
