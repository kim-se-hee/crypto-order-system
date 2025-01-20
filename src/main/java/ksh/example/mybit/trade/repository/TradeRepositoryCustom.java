package ksh.example.mybit.trade.repository;

import ksh.example.mybit.trade.domain.Trade;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeRepositoryCustom {

    List<Trade> findByMemberIdAndCoinId(Long memberId, Long CoinId, Pageable pageable);
}
