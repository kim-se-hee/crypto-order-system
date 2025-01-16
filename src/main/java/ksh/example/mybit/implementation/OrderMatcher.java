package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.Trade;
import ksh.example.mybit.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Component
@RequiredArgsConstructor
public class OrderMatcher {
    private final TradeRepository tradeRepository;


    public Trade match(Order order, Order matchingOrder) {

        Integer tradeVolume = calculateTradeVolume(order, matchingOrder);

        updateOrderAmount(order, matchingOrder, tradeVolume);

        Trade executedTrade = new Trade(
                new BigDecimal(tradeVolume).divide(order.getCoin().getPrice(), 8, RoundingMode.HALF_UP),
                order.getCoin().getPrice(),
                tradeVolume,
                order,
                matchingOrder);
        tradeRepository.save(executedTrade);

        return executedTrade;

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
