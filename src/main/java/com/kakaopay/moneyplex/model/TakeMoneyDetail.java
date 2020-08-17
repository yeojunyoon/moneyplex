package com.kakaopay.moneyplex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TakeMoneyDetail {
    private Long receiveMoney;
    private Long receiveUserId;
}
