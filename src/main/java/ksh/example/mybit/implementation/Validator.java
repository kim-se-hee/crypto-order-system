package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.*;
import ksh.example.mybit.repository.CoinRepository;
import ksh.example.mybit.repository.MemberCoinRepository;
import ksh.example.mybit.repository.MemberRepository;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validator {
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;
    private final MemberCoinRepository memberCoinRepository;
    private final OrderRepository orderRepository;

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

        checkAvailableOrderAmount(order);
    }

    private void checkAvailableOrderAmount(Order order) {
        OrderSide orderSide = order.getOrderSide();

        if (orderSide == OrderSide.SELL) {
            checkAvailableCoinAmount(order);
            return;
        }

        checkAvailableKoreanWonAmount(order);
    }

    private void checkAvailableCoinAmount(Order order) {
        Member member = order.getMember();
        Coin coin = order.getCoin();

        MemberCoin memberCoin = memberCoinRepository.findByMemberAndCoin(member, coin)
                .orElseThrow(() -> new IllegalArgumentException("보유 수량보다 매도 수량이 많습니다."));

        Long pendingAmount = orderRepository.sumPendingOrderAmount(OrderSide.SELL, member, coin);
        long availableAmount = memberCoin.getKoreanWonValue() - pendingAmount;

        if (availableAmount < order.getAmount()) {
            throw new IllegalArgumentException("주문 가능한 수량이 부족합니다");
        }
    }

    private void checkAvailableKoreanWonAmount(Order order) {
        Member member = order.getMember();

        MemberCoin memberCoin = memberCoinRepository.findByMemberAndCoinTicker(member, "won")
                .orElseThrow(() -> new IllegalArgumentException("원화가 부족합니다."));

        Long pendingAmount = orderRepository.sumPendingOrderAmount(OrderSide.BUY, member, null);
        long availableAmount = memberCoin.getKoreanWonValue() - pendingAmount;

        if (availableAmount < order.getAmount()) {
            throw new IllegalArgumentException("주문 가능한 금액이 부족합니다");
        }
    }

}
