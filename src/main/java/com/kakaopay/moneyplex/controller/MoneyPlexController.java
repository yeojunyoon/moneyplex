package com.kakaopay.moneyplex.controller;

import com.kakaopay.moneyplex.model.ApiResponse;
import com.kakaopay.moneyplex.model.MoneyPlex;
import com.kakaopay.moneyplex.model.ResponseData;
import com.kakaopay.moneyplex.constants.HeaderField;
import com.kakaopay.moneyplex.entity.Plex;
import com.kakaopay.moneyplex.entity.TakeMoney;
import com.kakaopay.moneyplex.service.PlexService;
import com.kakaopay.moneyplex.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MoneyPlexController {

    private final PlexService plexService;

    @PostMapping("/plex")
    public ApiResponse moneyPlex(
            @RequestHeader(HeaderField.USER_ID) Long userId,
            @RequestHeader(HeaderField.ROOM_ID) String roomId,
            @RequestBody MoneyPlex moneyPlex) {
        moneyPlex.setUserId(userId);
        moneyPlex.setRoomId(roomId);

        ResponseData responseData = new ResponseData(plexService.plex(moneyPlex));

        log.info("CREATE SUCCESS PLEX TOKEN {}", GsonUtil.toString(responseData));
        return new ApiResponse(responseData);
    }

    @PutMapping("/take/{token}")
    public ApiResponse takeMoneyByToken(
            @RequestHeader(HeaderField.USER_ID) Long userId,
            @RequestHeader(HeaderField.ROOM_ID) String roomId,
            @PathVariable("token") String token) {

        TakeMoney takeMoney = plexService.takeMoneyByToken(userId, roomId, token);
        ResponseData responseData = new ResponseData(takeMoney.getAmount());

        log.info("TAKE MONEY {}", GsonUtil.toString(responseData));
        return new ApiResponse(responseData);
    }

    @GetMapping("/plex/{token}")
    public ApiResponse plexInfo(
            @RequestHeader(HeaderField.USER_ID) Long userId,
            @RequestHeader(HeaderField.ROOM_ID) String roomId,
            @PathVariable("token") String token) {

        Plex plexInfo = plexService.getPlexInfo(userId, roomId, token);
        ResponseData responseData = new ResponseData(plexInfo);

        log.info("PLEX INFO {}", GsonUtil.toString(responseData));
        return new ApiResponse(responseData);
    }
}
