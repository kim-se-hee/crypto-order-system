package ksh.example.mybit.order.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.implementaion.CoinReader;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.implementaion.MemberReader;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.dto.request.OrderCreateRequestDto;
import ksh.example.mybit.order.repository.OrderRepository;
import ksh.example.mybit.order.service.dto.request.OrderCreateServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final MemberReader memberReader;
    private final CoinReader coinReader;
    private final OrderRepository orderRepository;

    public Order create(OrderCreateServiceRequest requestDto) {
        Member member = memberReader.readById(requestDto.getMemberId());
        Coin coin = coinReader.readById(requestDto.getCoinId());

        Order order = Order.builder()
                .volume(requestDto.getOrderVolume())
                .orderSide(requestDto.getOrderSide())
                .orderType(requestDto.getOrderType())
                .limitPrice(requestDto.getLimitPrice())
                .member(member)
                .coin(coin)
                .build();

        return orderRepository.save(order);
    }

    public void cancel(Long orderId) {
        orderRepository.findById(orderId)
                .ifPresent(Order::cancel);
    }

    public void activateTriggeredOrders() {
        orderRepository.updateOrderStatusOfTriggeredOrders();
    }
}
