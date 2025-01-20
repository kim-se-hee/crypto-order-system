package ksh.example.mybit.trade.repository;

import ksh.example.mybit.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long>, TradeRepositoryCustom {
}
