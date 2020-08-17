package com.kakaopay.moneyplex.model;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyPlex {
    private Long userId;
    private String roomId;
    private long amount;
    private long targetCount;

}
