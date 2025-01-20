package ksh.example.mybit.trade.service;

import ksh.example.mybit.trade.domain.Trade;
import ksh.example.mybit.trade.implementation.TradeReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeReader tradeReader;

    public List<Trade> getTradeHistory(Long memberId, Long coinId, Pageable pageable) {
        return tradeReader.readTradeBy(memberId, coinId, pageable);
    }
}
