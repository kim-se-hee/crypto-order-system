package ksh.example.mybit.service;

import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.implementation.TradeReader;
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
