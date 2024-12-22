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

    public void checkEmailIsAvailable(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {throw new IllegalArgumentException("해당 이메일은 사용할 수 없습니다.");});
    }

    public void checkMemberIsValid(Member member) {
        memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public void checkMarketSupports(Coin coin) {
        coinRepository.findById(coin.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코인입니다."));
    }

    public void checkOrderIsValid(Order order) {
        checkMemberIsValid(order.getMember());

        checkMarketSupports(order.getCoin());

        checkWalletBefore(order);
    }

    private void checkWalletBefore(Order order) {
        OrderSide orderSide = order.getOrderSide();

        if (orderSide == OrderSide.SELL) {
            checkCoinInWallet(order);
            return;
        }

        checkKoreanWonInWallet(order);
    }

    private void checkCoinInWallet(Order order) {
        Member member = order.getMember();
        Coin coin = order.getCoin();
        Integer orderAmount = order.getAmount();

        memberCoinRepository.findByMemberAndCoin(member, coin)
                .filter(memberCoin -> memberCoin.getKoreanWonValue() >= orderAmount)
                .orElseThrow(() -> new IllegalArgumentException("보유 수량보다 매도 수량이 많습니다."));

    }

    private void checkKoreanWonInWallet(Order order) {
        Member member = order.getMember();
        Integer orderAmount = order.getAmount();

        memberCoinRepository.findByMemberAndCoinTicker(member, "won")
                .filter(memberCoin -> memberCoin.getKoreanWonValue() >= orderAmount)
                .orElseThrow(() -> new IllegalArgumentException("원화가 부족합니다."));
    }

}
