package web.mvc.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.mvc.exception.BoardException;
import web.mvc.exception.DateRequestException;
import web.mvc.exception.MemberException;
import web.mvc.exception.PlaceException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, String>> handleMemberException(MemberException e) {

        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<Map<String, String>> handleBoardException(BoardException e) {

        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(PlaceException.class)
    public ResponseEntity<Map<String, String>> handlePlaceException(PlaceException e) {

        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(DateRequestException.class)
    public ResponseEntity<Map<String, String>> handleDateRequestException(DateRequestException e) {

        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

}
