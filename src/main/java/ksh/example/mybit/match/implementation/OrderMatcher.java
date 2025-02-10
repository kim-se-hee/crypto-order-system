package ksh.example.mybit.match.implementation;

import ksh.example.mybit.global.util.BigDecimalCalculateUtil;
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
        Order buyOrder = decideBuyOrder(order, matchingOrder);
        Order sellOrder = decideSellOrder(order, matchingOrder);

        Integer tradeVolume = calculateTradeVolume(buyOrder, sellOrder);

        updateOrderVolume(buyOrder, sellOrder, tradeVolume);

        BigDecimal executionPrice = decideExecutionPrice(buyOrder, sellOrder);
        BigDecimal executedQuantity = BigDecimalCalculateUtil
                .init(tradeVolume)
                .divide(executionPrice, 8, RoundingMode.FLOOR)
                .getValue();

        Trade executedTrade = Trade.builder()
                .executedQuantity(executedQuantity)
                .executedPrice(executionPrice)
                .executedVolume(tradeVolume)
                .buyOrder(buyOrder)
                .sellOrder(sellOrder)
                .build();

        tradeRepository.save(executedTrade);

        return executedTrade;

    }

    private static Order decideBuyOrder(Order order, Order matchingOrder) {
        if(order.getOrderSide() == OrderSide.BUY) {
            return order;
        }

        return matchingOrder;
    }

    private static Order decideSellOrder(Order order, Order matchingOrder) {
        if(order.getOrderSide() == OrderSide.SELL) {
            return order;
        }

        return matchingOrder;
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
