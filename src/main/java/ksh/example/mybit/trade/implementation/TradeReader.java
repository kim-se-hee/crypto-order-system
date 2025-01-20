package ksh.example.mybit.trade.implementation;

import ksh.example.mybit.trade.domain.Trade;
import ksh.example.mybit.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TradeReader {

    private final TradeRepository tradeRepository;

    public List<Trade> readTradeBy(Long memberId, Long coinId, Pageable pageable) {
        return tradeRepository.findByMemberIdAndCoinId(memberId, coinId, pageable);
    }
}
