package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.OrderType;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoinUpdater {

    private final CoinRepository coinRepository;

    public void updatePrice(Trade trade){
        Coin coin = trade.getSellOrder().getCoin();
        coin.updatePrice(trade.getExecutedPrice());
    }

}
