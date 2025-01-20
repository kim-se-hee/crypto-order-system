package ksh.example.mybit.coin.implementaion;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.trade.domain.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinUpdater {

    private final CoinRepository coinRepository;

    public void updatePrice(Trade trade) {
        Coin coin = trade.getSellOrder().getCoin();
        coin.updatePrice(trade.getExecutedPrice());
    }

}
