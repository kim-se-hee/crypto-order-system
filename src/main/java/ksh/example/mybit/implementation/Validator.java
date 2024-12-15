package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.repository.CoinRepository;
import ksh.example.mybit.repository.MemberCoinRepository;
import ksh.example.mybit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validator {
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;
    private final MemberCoinRepository memberCoinRepository;

    public void checkMemberIsValid(Member member) {
        memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public void checkMarketSupports(Coin coin) {
        coinRepository.findById(coin.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코인입니다."));
    }

    public void checkWallet(Order order, Member member, Coin coin) {
        OrderSide orderSide = order.getOrderSide();
        Integer orderAmount = order.getAmount();

        if (orderSide == OrderSide.SELL) {
            checkCoinInWallet(orderAmount, member, coin);
            return;
        }

        checkKoreanWonInWallet(member, orderAmount);
    }

    private void checkCoinInWallet(Integer orderAmount, Member member, Coin coin) {
        memberCoinRepository.findByMemberAndCoin(member, coin)
                .filter(memberCoin -> memberCoin.getKoreanWonValue() >= orderAmount)
                .orElseThrow(() -> new IllegalArgumentException("보유 수량보다 매도 수량이 많습니다."));

    }

    private void checkKoreanWonInWallet(Member member, Integer orderAmount) {
        memberCoinRepository.findByMemberAndCoinTicker(member, "won")
                .filter(memberCoin -> memberCoin.getKoreanWonValue() >= orderAmount)
                .orElseThrow(() -> new IllegalArgumentException("원화가 부족합니다."));
    }
}
