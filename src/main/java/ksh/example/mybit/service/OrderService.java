package ksh.example.mybit.service;

import ksh.example.mybit.controller.form.OrderForm;
import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.MemberCoin;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.repository.CoinRepository;
import ksh.example.mybit.repository.MemberCoinRepository;
import ksh.example.mybit.repository.MemberRepository;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;
    private final MemberCoinRepository memberCoinRepository;

    public Order addSellOrder(OrderForm orderForm){
        Long memberId = orderForm.getMemberId();
        Member member = checkMemberIsValid(memberId);

        Long coinId = orderForm.getCoinId();
        Coin coin = checkCoinIsValid(coinId);

        MemberCoin memberCoin = memberCoinRepository.findByMemberAndCoin(member, coin);
        if(memberCoin == null)
            throw new IllegalArgumentException("보유하고 있지 않은 코인입니다.");

        Integer orderAmount = orderForm.getOrderAmount();
        if(memberCoin.isLessThan(orderAmount))
            throw new IllegalArgumentException("보유 수량보다 매도 수량이 많습니다.");

        Order order = new Order(
                orderAmount,
                orderForm.getOrderSide(),
                orderForm.getOrderType(),
                orderForm.getLimitPrice(),
                member,
                coin);
        orderRepository.save(order);
        return order;
    }

    private Member checkMemberIsValid(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty())
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        return optionalMember.get();
    }

    private Coin checkCoinIsValid(Long coinId) {
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if(optionalCoin.isEmpty())
            throw new IllegalArgumentException("존재하지 않는 코인입니다.");
        return optionalCoin.get();
    }

}
