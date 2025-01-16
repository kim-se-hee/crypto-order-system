package ksh.example.mybit.persistence.mysql.jpa.repository;

import ksh.example.mybit.persistence.mysql.jpa.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
}
