package ksh.example.mybit.coin.repository;

import ksh.example.mybit.coin.domain.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
}
