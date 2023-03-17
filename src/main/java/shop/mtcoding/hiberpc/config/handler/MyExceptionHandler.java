package shop.mtcoding.hiberpc.config.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.hiberpc.config.exception.MyException;

@RestControllerAdvice
public class MyExceptionHandler {
    // runtime Exception만 낚아챈다.

    @ExceptionHandler(MyException.class)
    public ResponseEntity<?> error1(MyException e) {
        // 원래 여기다가 DTO 넘겨줄 수 있음
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}
