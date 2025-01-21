package ksh.example.mybit.membercoin.service;

import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.dto.response.WalletAssetListResponseDto;
import ksh.example.mybit.membercoin.implementation.WalletReader;
import ksh.example.mybit.membercoin.implementation.WalletUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCoinService {

    private final WalletReader walletReader;
    private final WalletUpdater walletUpdater;

    @Transactional
    public void deposit(Long memberId, Long coinId, Integer amount) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(memberId, coinId);

        walletUpdater.increaseBalanceOf(memberCoin, amount);
    }

    @Transactional
    public void withdraw(Long memberId, Long coinId, Integer amount) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(memberId, coinId);

        if (memberCoin.getBalance() < amount) {
            throw new IllegalArgumentException("보유 수량이 부족합니다");
        }

        walletUpdater.decreaseBalanceOf(memberCoin, amount);
    }

    @Transactional
    public WalletAssetListResponseDto findAllCoinsInWallet(Long id) {
        List<MemberCoin> memberCoins = walletReader.readAllCoinOfMember(id);

        return new WalletAssetListResponseDto(memberCoins);
    }
}
