package ksh.example.mybit.persistence.mysql.jpa.repository;

import ksh.example.mybit.persistence.mysql.jpa.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
