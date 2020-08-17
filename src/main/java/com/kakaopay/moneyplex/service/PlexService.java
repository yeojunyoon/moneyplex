package com.kakaopay.moneyplex.service;

import com.kakaopay.moneyplex.model.MoneyPlex;
import com.kakaopay.moneyplex.constants.StatusCode;
import com.kakaopay.moneyplex.entity.Plex;
import com.kakaopay.moneyplex.entity.TakeMoney;
import com.kakaopay.moneyplex.exception.MoneyPlexException;
import com.kakaopay.moneyplex.repository.PlexRepository;
import com.kakaopay.moneyplex.repository.TakeMoneyRepository;
import com.kakaopay.moneyplex.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlexService {
    private final PlexRepository plexRepository;
    private final TakeMoneyRepository takeMoneyRepository;

    public Plex getPlexInfo(Long userId, String roomId, String token) throws MoneyPlexException {
        LocalDateTime time = LocalDateTime.now();
        time = time.minusDays(7);
        Optional<Plex> plex = plexRepository.findByTokenAndRoomIdAndOwnerIdAndCreatedTimeAfter(
                token, roomId, userId, time);
        plex.orElseThrow(()->new MoneyPlexException(StatusCode.E1405));
        return plex.get();
    }

    @Transactional
    public String plex(MoneyPlex moneyPlex) throws MoneyPlexException{

        if(moneyPlex.getTargetCount() < 1 || moneyPlex.getAmount() < 1){
            throw new MoneyPlexException(StatusCode.E1300);
        }

        if(moneyPlex.getTargetCount() > moneyPlex.getAmount()){
            throw new MoneyPlexException(StatusCode.E1301);
        }

        String token = createToken(moneyPlex);
        Plex p = new Plex(token, moneyPlex);
        p = plexRepository.save(p);

        takeMoneyRepository.saveAll(divideMoney(p));

        return token;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TakeMoney takeMoneyByToken(Long userId, String roomId, String token) throws MoneyPlexException {

        LocalDateTime time = LocalDateTime.now();
        time = time.minusMinutes(10);
        Optional<Plex> plex = plexRepository.findByTokenAndRoomIdAndCreatedTimeAfter(token, roomId, time);
        plex.orElseThrow(()->new MoneyPlexException(StatusCode.E1401));

        if(plex.get().getOwnerId().equals(userId)){
            throw new MoneyPlexException(StatusCode.E1402);
        }

        log.info("TOKEN {}, AMOUT {}, TARGETCOUNT {}",
                plex.get().getToken(), plex.get().getAmount(), plex.get().getTargetCount());

        TakeMoney takeMoney = null;
        for(TakeMoney money : plex.get().getTakeMoneyList()) {
            if(money.getReceiveUserId() != null && money.getReceiveUserId().equals(userId)){
                throw new MoneyPlexException(StatusCode.E1403);
            }else if(money.getReceiveUserId() == null && takeMoney == null) {
                takeMoney = money;
            }
        }

        if(takeMoney == null){
            throw new MoneyPlexException(StatusCode.E1404);
        }

        takeMoney.take(userId);
        log.info("takemoney get {} : {} : {}", takeMoney.getId(), takeMoney.getAmount(), takeMoney.getReceiveUserId());
        return takeMoney;
    }

    public List<TakeMoney> divideMoney(Plex plex){

        List<TakeMoney> divs = new ArrayList<TakeMoney>();
        Long amount = plex.getAmount() - plex.getTargetCount();
        Long divided = 0l;

        for(long i=0; i<plex.getTargetCount()-1; i++){
            Long plusMoney = 1l;
            if(amount-divided > 2){
                plusMoney += RandomUtil.getRandLong(amount-divided);
            }
            log.info("div money {}", plusMoney);
            divs.add(new TakeMoney(plex, null, plusMoney));
            divided += plusMoney;
        }
        log.info("div money {}", plex.getAmount() - divided);
        divs.add(new TakeMoney(plex, null, plex.getAmount() - divided));

        return divs;
    }

    public String createToken(MoneyPlex moneyPlex){

        String token = RandomUtil.getRandomString(3);
        LocalDateTime time = LocalDateTime.now();
        time.minusMinutes(10);
        while(plexRepository.countByTokenAndOwnerIdAndRoomIdAndCreatedTimeAfter(
                token, moneyPlex.getUserId(), moneyPlex.getRoomId(), time) > 0){
            token = RandomUtil.getRandomString(3);
        }
        return token;
    }
}
