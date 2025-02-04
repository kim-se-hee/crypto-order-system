package ksh.example.mybit.trade.service;

import ksh.example.mybit.trade.domain.Trade;
import ksh.example.mybit.trade.service.dto.response.TradeHistoryListResponse;
import ksh.example.mybit.trade.implementation.TradeReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeReader tradeReader;

    public TradeHistoryListResponse getTradeHistory(Long memberId, Long coinId, Pageable pageable) {
        List<Trade> trades = tradeReader.readTradeBy(memberId, coinId, pageable);

        return new TradeHistoryListResponse(trades, memberId);
    }
}
