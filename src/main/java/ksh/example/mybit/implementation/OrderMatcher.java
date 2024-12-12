package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderStatus;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.repository.OrderRepository;
import ksh.example.mybit.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OrderMatcher {
    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;

    public Optional<Trade> match() {
        Order order = orderRepository.findFirstByOrderStatusOrderByCreatedAtAsc(OrderStatus.PENDING);
        Order matchingOrder = orderRepository.findMatchingOrder(order);

        if (matchingOrder == null) {
            return Optional.empty();
        }

        Integer tradeVolume = calculateTradeVolume(order, matchingOrder);

        updateOrderAmount(order, matchingOrder, tradeVolume);

        Trade executedTrade = new Trade(
                order.getCoin().getPrice(),
                tradeVolume,
                order,
                matchingOrder);
        tradeRepository.save(executedTrade);

        return Optional.of(executedTrade);

    }

    private static void updateOrderAmount(Order order, Order matchingOrder, Integer tradeVolume) {
        order.updateAmount(tradeVolume);
        matchingOrder.updateAmount(tradeVolume);

        if (matchingOrder.isFinished()) {
            matchingOrder.finish();
        }

        if (order.isFinished()) {
            order.finish();
        }
    }

    private Integer calculateTradeVolume(Order buyOrder, Order sellOrder) {
        Integer buyAmount = buyOrder.getAmount();
        Integer sellAmount = sellOrder.getAmount();
        return Math.min(buyAmount, sellAmount);
    }

}
