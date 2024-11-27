package ksh.example.mybit.repository;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.MemberCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberCoinRepository extends JpaRepository<MemberCoin, Long> {
    MemberCoin findByMemberAndCoin(Member member, Coin coin);

    @Query("select mc from MemberCoin mc where mc.member = :member and mc.coin.ticker = 'won'")
    MemberCoin findKoreanWonByMember(@Param("member") Member member);
}
