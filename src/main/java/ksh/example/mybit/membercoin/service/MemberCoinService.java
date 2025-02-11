package ksh.example.mybit.membercoin.service;

import jakarta.validation.Valid;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import ksh.example.mybit.membercoin.service.dto.request.FundTransferServiceRequest;
import ksh.example.mybit.membercoin.service.dto.request.InvestmentStaticsServiceRequest;
import ksh.example.mybit.membercoin.service.dto.response.InvestmentStaticsResponse;
import ksh.example.mybit.membercoin.service.dto.response.WalletAssetListResponse;
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
    public void deposit(FundTransferServiceRequest request) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(request.getMemberId(), request.getCoinId());

        walletUpdater.increaseQuantityOf(memberCoin, request.getQuantity());
    }

    @Transactional
    public void withdraw(FundTransferServiceRequest request) {
        MemberCoin memberCoin = walletReader.readMemberCoinWithLock(request.getMemberId(), request.getCoinId());
        BigDecimal quantity = request.getQuantity();

        if (memberCoin.getQuantity().compareTo(quantity) < 0) {
            throw new IllegalArgumentException("보유 수량이 부족합니다");
        }

        walletUpdater.decreaseBalanceOf(memberCoin, quantity);
    }

    @Transactional
    public WalletAssetListResponse findAllCoinsInWallet(Long id) {
        List<MemberCoin> memberCoins = walletReader.readAllCoinOfMember(id);

        return new WalletAssetListResponse(memberCoins);
    }


    @Transactional
    public InvestmentStaticsResponse getInvestmentStatic(@Valid InvestmentStaticsServiceRequest requestDto) {
        MemberCoin memberCoin = walletReader.readByMemberIdAndCoinId(requestDto.getMemberId(), requestDto.getCoinId());

        double balance = portfolioAnalyzer.calculateBalance(memberCoin);
        double ROI = portfolioAnalyzer.calculateROI(memberCoin);

        return new InvestmentStaticsResponse(memberCoin, balance, ROI);
    }
}
