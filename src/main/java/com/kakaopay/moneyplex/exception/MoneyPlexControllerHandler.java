package com.kakaopay.moneyplex.exception;

import com.kakaopay.moneyplex.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MoneyPlexControllerHandler {

    @ExceptionHandler(MoneyPlexException.class)
    public ResponseEntity<ApiResponse> handleMoneyPlexException(MoneyPlexException e) {
        log.error("[MoneyPlexControllerHandler][MoneyPlexException] {}", e.getDescription());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(Exception e) {
        log.error("[MoneyPlexControllerHandler][Exception] {}", e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.of(e));
    }
}
