package ksh.example.mybit.coin.implementaion;

import ksh.example.mybit.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinSelector {
    private final CoinRepository coinRepository;

    private long numberOfCoins;

    private long currentCoin = 0;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    private void getNumberOfCoins() {
        this.numberOfCoins = coinRepository.count() - 1;
    }

    public long getCurrentCoin() {
        long coinId = this.currentCoin + 1;
        this.currentCoin = (this.currentCoin + 1) % this.numberOfCoins;
        return coinId;
    }
}
