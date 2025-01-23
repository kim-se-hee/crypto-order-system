package ksh.example.mybit.membercoin.service;

import jakarta.validation.Valid;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.dto.request.InvestmentStaticsRequestDto;
import ksh.example.mybit.membercoin.dto.response.InvestmentStaticsResponseDto;
import ksh.example.mybit.membercoin.dto.response.WalletAssetListResponseDto;
import ksh.example.mybit.membercoin.implementation.PortfolioAnalyzer;
import ksh.example.mybit.membercoin.implementation.WalletReader;
import ksh.example.mybit.membercoin.implementation.WalletUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCoinService {

    private final WalletReader walletReader;
    private final WalletUpdater walletUpdater;
    private final PortfolioAnalyzer portfolioAnalyzer;

    @Transactional
    public void deposit(Long memberId, Long coinId, BigDecimal amount) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(memberId, coinId);

        walletUpdater.increaseQuantityOf(memberCoin, amount);
    }

    @Transactional
    public void withdraw(Long memberId, Long coinId, BigDecimal amount) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(memberId, coinId);

        if (memberCoin.getQuantity().compareTo(amount) < 0) {
            throw new IllegalArgumentException("보유 수량이 부족합니다");
        }

        walletUpdater.decreaseBalanceOf(memberCoin, amount);
    }

    @Transactional
    public WalletAssetListResponseDto findAllCoinsInWallet(Long id) {
        List<MemberCoin> memberCoins = walletReader.readAllCoinOfMember(id);

        return new WalletAssetListResponseDto(memberCoins);
    }

    @Transactional
    public InvestmentStaticsResponseDto getInvestmentStatic(@Valid InvestmentStaticsRequestDto requestDto) {
        MemberCoin memberCoin = walletReader.readByMemberIdAndCoinId(requestDto.getMemberId(), requestDto.getCoinId());

        double balance = portfolioAnalyzer.calculateBalance(memberCoin);
        BigDecimal ROI = portfolioAnalyzer.calculateROI(memberCoin);

        return new InvestmentStaticsResponseDto(memberCoin, balance, ROI);
    }
}
