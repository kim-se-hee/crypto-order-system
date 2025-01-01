package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.MemberCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberCoinRepository extends JpaRepository<MemberCoin, Long> {
    Optional<MemberCoin> findByMemberAndCoin(Member member, Coin coin);

    Optional<MemberCoin> findByMemberIdAndCoinId(Long memberId, Long coinId);

    Optional<MemberCoin> findByMemberAndCoinTicker(Member member, String coinTicker);

}
