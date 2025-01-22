package ksh.example.mybit.match.implementation;

import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderType;
import ksh.example.mybit.trade.domain.Trade;
import ksh.example.mybit.trade.repository.TradeRepository;
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

        Trade executedTrade = Trade.builder()
                .executedQuantity(new BigDecimal(tradeVolume).divide(order.getCoin().getPrice(), 8, RoundingMode.HALF_UP))
                .executedPrice(executionPrice)
                .executedVolume(tradeVolume)
                .buyOrder(order)
                .sellOrder(matchingOrder)
                .build();

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
        if (order.getOrderType() == OrderType.MARKET)
            return matchingOrder.getLimitPrice();

        if (matchingOrder.getOrderType() == OrderType.MARKET)
            return order.getLimitPrice();

        if (order.getOrderSide() == OrderSide.SELL) {
            return order.getLimitPrice();
        }

        return matchingOrder.getLimitPrice();
    }

}
