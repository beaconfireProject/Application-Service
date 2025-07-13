package com.rong.applicationservice.exception;

import com.rong.applicationservice.dto.response.GeneralResponse;
import com.rong.applicationservice.dto.response.ResponseFail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = StatusDuplicateException.class)
    public ResponseEntity<GeneralResponse> globalExceptionHandler(StatusDuplicateException statusDuplicateException) {
        GeneralResponse generalResponse = ResponseFail.builder()
                .success(false)
                .time(LocalDateTime.now())
                .error(statusDuplicateException.getMessage())
                .message(statusDuplicateException.getMessage())
                .status(403)
                .build();
        return ResponseEntity.status(403).body(generalResponse);
    }

//    @ExceptionHandler(value = RuntimeException.class)
//    public ResponseEntity<GeneralResponse> globalExceptionHandler(RuntimeException e) {
//        GeneralResponse generalResponse = ResponseFail.builder()
//                .success(false)
//                .time(LocalDateTime.now())
//                .error(e.getMessage())
//                .message(e.getMessage())
//                .status(403)
//                .build();
//        return ResponseEntity.status(403).body(generalResponse);
//    }
}
