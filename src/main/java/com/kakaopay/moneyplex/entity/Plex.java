package com.kakaopay.moneyplex.entity;

import com.kakaopay.moneyplex.model.MoneyPlex;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(indexes = {
        @Index(name = "unique_token_room_id_user_id_created_time", columnList = "token, roomId, ownerId, createdTime", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String token;
    @Column(nullable = false)
    private String roomId;
    @Column(nullable = false)
    private Long ownerId;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long targetCount;
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "plex", cascade = CascadeType.ALL)
    private List<TakeMoney> takeMoneyList = new ArrayList<>();

    public Plex(String token, MoneyPlex moneyPlex){
        this.token = token;
        this.roomId = moneyPlex.getRoomId();
        this.ownerId = moneyPlex.getUserId();
        this.amount = moneyPlex.getAmount();
        this.targetCount = moneyPlex.getTargetCount();
        this.createdTime = LocalDateTime.now();
    }
}
