package ksh.example.mybit.persistence.mysql.jpa.repository;

import jakarta.persistence.LockModeType;
import ksh.example.mybit.persistence.mysql.jpa.entity.Coin;
import ksh.example.mybit.persistence.mysql.jpa.entity.Member;
import ksh.example.mybit.persistence.mysql.jpa.entity.MemberCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MemberCoinRepository extends JpaRepository<MemberCoin, Long> {
    Optional<MemberCoin> findByMemberAndCoin(Member member, Coin coin);

    Optional<MemberCoin> findByMemberIdAndCoinId(Long memberId, Long coinId);

    Optional<MemberCoin> findByMemberAndCoinTicker(Member member, String coinTicker);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberCoin> findWithWriteLockByMemberAndCoin(Member member, Coin coin);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberCoin> findWithWriteLockByMemberIdAndCoinId(Long memberId, Long coinId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberCoin> findWithWriteLockByMemberAndCoinTicker(Member member, String coinTicker);

}
