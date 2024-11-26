package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.MemberCoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCoinRepository extends JpaRepository<MemberCoin, Long> {
    MemberCoin findByMemberAndCoin(Member member, Coin coin);
}
