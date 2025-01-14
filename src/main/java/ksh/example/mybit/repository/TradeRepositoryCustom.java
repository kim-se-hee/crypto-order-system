package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Trade;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeRepositoryCustom {

    List<Trade> findByMemberIdAndCoinId(Long memberId, Long CoinId, Pageable pageable);
}
