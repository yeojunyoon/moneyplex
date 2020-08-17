package com.kakaopay.moneyplex.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "unique_plex_id_receive_user_id", columnList = "plex_id, receiveUserId", unique = true)
})
public class TakeMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plex_id")
    private Plex plex;

    @Column(nullable = false)
    private Long amount;

    private Long receiveUserId;
    private LocalDateTime receiveTime;

    public TakeMoney(Plex plex, Long receiveUserId, Long amount){
        this.plex = plex;
        this.receiveUserId = receiveUserId;
        this.amount = amount;
    }

    public long take(Long receiveUserId){
        this.receiveUserId = receiveUserId;
        this.receiveTime = LocalDateTime.now();
        return amount;
    }
}
