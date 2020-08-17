package com.kakaopay.moneyplex.model;

import com.kakaopay.moneyplex.constants.StatusCode;
import com.kakaopay.moneyplex.exception.MoneyPlexException;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse {
    private boolean result;
    private String description;
    private ResponseData data;

    public ApiResponse(StatusCode code){
        result = code.getCode() == 1200;
        description = code.getDescription();
    }

    public ApiResponse(MoneyPlexException exp){
        result = exp.getResultCode() == 1200;
        description = exp.getDescription();
    }

    public ApiResponse(ResponseData data){
        result = true;
        description = "Success";
        this.data = data;
    }

    public static ApiResponse of(MoneyPlexException e){
        return new ApiResponse(e);
    }

    public static ApiResponse of(Exception e){
        return new ApiResponse(StatusCode.E1500);
    }

}
