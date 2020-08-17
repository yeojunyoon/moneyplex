package com.kakaopay.moneyplex.service;

import com.kakaopay.moneyplex.entity.Plex;
import com.kakaopay.moneyplex.entity.TakeMoney;
import com.kakaopay.moneyplex.model.MoneyPlex;
import com.kakaopay.moneyplex.repository.PlexRepository;
import com.kakaopay.moneyplex.repository.TakeMoneyRepository;
import com.kakaopay.moneyplex.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PlexServiceTest {
    @Autowired
    private PlexRepository plexRepository;
    @Autowired
    private TakeMoneyRepository takeMoneyRepository;

    private PlexService plexService;

    private MoneyPlex moneyPlex;
    private Plex plex;
    @BeforeEach
    public void setting(){
        moneyPlex = MoneyPlex.builder()
                .amount(10000)
                .roomId("_rH6sn")
                .targetCount(5)
                .userId(4387529387645l)
                .build();
        plexService = new PlexService(plexRepository, takeMoneyRepository);
    }

    @Test
    void plexService_saveTest(){
        String token = RandomUtil.getRandomString(3);
        System.out.println(token);
        this.plex = new Plex(token, moneyPlex);
        plexRepository.save(plex);
        System.out.println(plex.getId());
        assertNotNull(plex.getId());
        assertEquals(plex.getAmount(), moneyPlex.getAmount());
        assertEquals(plex.getRoomId(), moneyPlex.getRoomId());
        assertEquals(plex.getTargetCount(), moneyPlex.getTargetCount());
        assertEquals(plex.getOwnerId(), moneyPlex.getUserId());
    }

    @Test
    void plexService_divTest(){
        String token = RandomUtil.getRandomString(3);
        final Plex plex = new Plex(token, moneyPlex);
        List<TakeMoney> list = plexService.divideMoney(plex);
        assertEquals(list.size(), moneyPlex.getTargetCount());
        AtomicReference<Long> amount = new AtomicReference<>(0l);
        list.forEach((takeMoney) -> {
            amount.updateAndGet(v -> v + takeMoney.getAmount());
        });
        assertEquals(amount.get(), moneyPlex.getAmount());
    }

    @Test
    void plexService_plexInfo(){
        plexService_saveTest();

        Plex localPlex = plexService.getPlexInfo(plex.getOwnerId(), plex.getRoomId(), plex.getToken());

        assertEquals(localPlex, plex);
    }

    @Test
    void plexService_create(){
        String token = plexService.plex(moneyPlex);
        System.out.println(token);
    }
}