package com.rong.applicationservice.exception;

import com.rong.applicationservice.dto.response.GeneralResponse;
import com.rong.applicationservice.dto.response.ResponseFail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = StatusDuplicateException.class)
    public ResponseEntity<GeneralResponse> statusExceptionHandler(StatusDuplicateException statusDuplicateException) {
        GeneralResponse generalResponse = ResponseFail.builder()
                .success(false)
                .time(LocalDateTime.now())
                .error("Bad Request")
                .message(statusDuplicateException.getMessage())
                .status(403)
                .build();
        return ResponseEntity.status(403).body(generalResponse);
    }

    @ExceptionHandler(value = AuthorizationNotFoundException.class)
    public ResponseEntity<GeneralResponse> authorizationExceptionHandler(AuthorizationNotFoundException authorizationNotFoundException) {
        GeneralResponse generalResponse = ResponseFail.builder()
                .success(false)
                .time(LocalDateTime.now())
                .error("Bad Request")
                .message(authorizationNotFoundException.getMessage())
                .status(403)
                .build();
        return ResponseEntity.status(403).body(generalResponse);
    }

    @ExceptionHandler(value = ApplicationNotFoundException.class)
    public ResponseEntity<GeneralResponse> ApplicationNotFoundExceptionHandler(AuthorizationNotFoundException e) {
        GeneralResponse generalResponse = ResponseFail.builder()
                .success(false)
                .time(LocalDateTime.now())
                .error("Bad Request")
                .message(e.getMessage())
                .status(403)
                .build();
        return ResponseEntity.status(403).body(generalResponse);
    }

    @ExceptionHandler(value = EmployeeException.class)
    public ResponseEntity<GeneralResponse> CreationEmployeeExceptionHandler(EmployeeException e) {
        GeneralResponse generalResponse = ResponseFail.builder()
                .success(false)
                .time(LocalDateTime.now())
                .error("Bad Request")
                .message(e.getMessage())
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
