package ksh.example.mybit.service;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;
import ksh.example.mybit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final OrderRepository orderRepository;

    @Transactional
    public void matchOrder(){
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            matchOrdersWith(order);
        }
    }

    private void matchOrdersWith(Order order) {
        List<Order> matchingOrders = getMathcingOrderList(order);

        for(Order matchingOrder : matchingOrders){
            Integer tradeVolume = calculateTradeVolume(order, matchingOrder);

            order.updateAmount(tradeVolume);
            matchingOrder.updateAmount(tradeVolume);

            if(matchingOrder.isFinished()){
                matchingOrder.finish();
            }

            if(order.isFinished()){
                order.finish();
                break;
            }
        }
    }

    private List<Order> getMathcingOrderList(Order order) {
        Long coinId = order.getCoin().getId();
        OrderSide orderSide = order.getOrderSide();
        OrderType orderType = order.getOrderType();
        BigDecimal limitPrice = order.getLimitPrice();

        return orderRepository.findMatchingOrders(coinId, orderSide, orderType, limitPrice);
    }

    private static Integer calculateTradeVolume(Order buyOrder, Order sellOrder) {
        Integer buyAmount = buyOrder.getAmount();
        Integer sellAmount = sellOrder.getAmount();
        return Math.min(buyAmount, sellAmount);
    }
}
