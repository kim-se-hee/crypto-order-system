package ksh.example.mybit.implementation;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;
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

        updateOrderVolume(order, matchingOrder, tradeVolume);

        BigDecimal executionPrice = decideExecutionPrice(order, matchingOrder);

        Trade executedTrade = new Trade(
                new BigDecimal(tradeVolume).divide(order.getCoin().getPrice(), 8, RoundingMode.HALF_UP),
                executionPrice,
                tradeVolume,
                order,
                matchingOrder);
        tradeRepository.save(executedTrade);

        return executedTrade;

    }

    private static void updateOrderVolume(Order order, Order matchingOrder, Integer tradeVolume) {
        order.updateVolume(tradeVolume);
        matchingOrder.updateVolume(tradeVolume);

        if (matchingOrder.isFinished()) {
            matchingOrder.finish();
        }

        if (order.isFinished()) {
            order.finish();
        }
    }

    private Integer calculateTradeVolume(Order buyOrder, Order sellOrder) {
        Integer buyOrderVolume = buyOrder.getVolume();
        Integer sellOrderVolume = sellOrder.getVolume();
        return Math.min(buyOrderVolume, sellOrderVolume);
    }

    private BigDecimal decideExecutionPrice(Order order, Order matchingOrder) {
        if(order.getOrderType() == OrderType.MARKET)
            return matchingOrder.getLimitPrice();

        if(matchingOrder.getOrderType() == OrderType.MARKET)
            return order.getLimitPrice();

        if(order.getOrderSide() == OrderSide.SELL) {
            return order.getLimitPrice();
        }

        return matchingOrder.getLimitPrice();
    }

}
