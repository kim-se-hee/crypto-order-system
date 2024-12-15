package ksh.example.mybit.service;

import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.implementation.Validator;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Validator validator;
    private final OrderRepository orderRepository;

    public Order placeOrder(Order order) {
        Member member = order.getMember();
        validator.checkMemberIsValid(member);

        Coin coin = order.getCoin();
        validator.checkMarketSupports(coin);

        validator.checkWallet(order, member, coin);

        orderRepository.save(order);
        return order;
    }

}
