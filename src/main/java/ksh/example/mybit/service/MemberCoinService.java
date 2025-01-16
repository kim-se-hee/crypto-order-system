package ksh.example.mybit.service;

import ksh.example.mybit.persistence.mysql.jpa.entity.MemberCoin;
import ksh.example.mybit.implementation.WalletReader;
import ksh.example.mybit.implementation.WalletUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCoinService {

    private final WalletReader walletReader;
    private final WalletUpdater walletUpdater;

    @Transactional
    public void deposit(Long memberId, Long coinId, Integer amount) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(memberId, coinId);

        walletUpdater.increaseAmountOf(memberCoin, amount);
    }

    @Transactional
    public void withdraw(Long memberId, Long coinId, Integer amount) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(memberId, coinId);

        if(memberCoin.getKoreanWonValue() < amount) {
            throw new IllegalArgumentException("보유 수량이 부족합니다");
        }

        walletUpdater.decreaseAmountOf(memberCoin, amount);
    }
}
