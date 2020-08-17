package com.kakaopay.moneyplex.controller;

import com.kakaopay.moneyplex.constants.StatusCode;
import com.kakaopay.moneyplex.exception.MoneyPlexException;
import com.kakaopay.moneyplex.model.ApiResponse;
import com.kakaopay.moneyplex.model.MoneyPlex;
import com.kakaopay.moneyplex.service.PlexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoneyPlexControllerTest {
    @Autowired
    private PlexService plexService;

    private MoneyPlex moneyPlex;
    private MoneyPlexController moneyPlexController;

    @BeforeEach
    void setUp() {
        moneyPlexController = new MoneyPlexController(plexService);
    }

    @Test
    void moneyPlex_success() {
        MoneyPlex moneyPlex = MoneyPlex.builder()
                .amount(10000)
                .roomId("_rH6sn")
                .targetCount(5)
                .userId(4387529387645l)
                .build();
        ApiResponse apiResponse = moneyPlexController.moneyPlex(472876l, "_rHusq", moneyPlex);
        assertEquals(apiResponse.getDescription(), "Success");
    }

    @Test
    void moneyPlex_fail() {
        MoneyPlex moneyPlex = MoneyPlex.builder()
                .amount(10000)
                .roomId("_rH6sn")
                .targetCount(0)
                .userId(4387529387645l)
                .build();
        try{
            ApiResponse apiResponse = moneyPlexController.moneyPlex(472876l, "_rHusq", moneyPlex);
        }catch(MoneyPlexException e){
            assertEquals(e.getResultCode(), StatusCode.E1300.getCode());
        }
    }

    @Test
    void moneyPlex_fail2() {
        MoneyPlex moneyPlex = MoneyPlex.builder()
                .amount(100)
                .roomId("_rH6sn")
                .targetCount(101)
                .userId(4387529387645l)
                .build();
        try{
            ApiResponse apiResponse = moneyPlexController.moneyPlex(472876l, "_rHusq", moneyPlex);
        }catch(MoneyPlexException e){
            assertEquals(e.getResultCode(), StatusCode.E1301.getCode());
        }
    }

    @Test
    void takeMoneyByToken_fail() {
        try{
            ApiResponse apiResponse = moneyPlexController.takeMoneyByToken(472876l, "_rHusq", "Hwk");
        }catch(MoneyPlexException e){
            assertEquals(e.getResultCode(), StatusCode.E1401.getCode());
        }
    }

    @Test
    void plexInfo() {
        try{
            ApiResponse apiResponse = moneyPlexController.plexInfo(472876l, "_rHusq", "Hwk");
        }catch(MoneyPlexException e){
            assertEquals(e.getResultCode(), StatusCode.E1405.getCode());
        }
    }
}