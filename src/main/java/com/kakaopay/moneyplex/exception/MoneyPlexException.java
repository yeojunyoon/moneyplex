package com.kakaopay.moneyplex.exception;

import com.kakaopay.moneyplex.constants.StatusCode;
import lombok.Getter;

@Getter
public class MoneyPlexException extends RuntimeException {
    private String description;
    private int resultCode;
    
    public MoneyPlexException(StatusCode statusCode){
        this.resultCode = statusCode.getCode();
        this.description = statusCode.getDescription();
    }
}