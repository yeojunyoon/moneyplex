package com.kakaopay.moneyplex.constants;

public enum StatusCode {
    E1300(1300, "잘못된 요청 입니다"),
    E1301(1301, "금액이 인원수 보다 작습니다."),

    E1401(1401, "이미 만료되었거나 존재하지 않는 받기 입니다."),
    E1402(1402, "본인이 뿌리기 한 금액은 받을 수 없습니다."),
    E1403(1403, "이미 받았습니다."),
    E1404(1404, "선착순 받기가 모두 끝났습니다."),
    E1405(1405, "요청하신 토큰이 7일이 경과 하였거나, 유효하지 않은 토큰 입니다."),
    E1500(1500, "Internal Server Error"),
    ;

    private int code;
    private String description;

    StatusCode(int code, String description){
        this.code = code;
        this.description = description;
    }

    public int getCode(){
        return code;
    }
    public String getDescription(){
        return description;
    }
}
