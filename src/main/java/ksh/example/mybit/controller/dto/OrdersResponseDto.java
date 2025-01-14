package ksh.example.mybit.controller.dto;

import ksh.example.mybit.domain.Order;
import ksh.example.mybit.domain.OrderSide;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@Setter
public class OrdersResponseDto {

    List<OrderResponseDto> orderResponses;

    public OrdersResponseDto(List<Order> orders) {
        this.orderResponses = orders.stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    @Getter
    @Setter
    static class OrderResponseDto {

        OrderSide orderSide;
        String ticker;
        Integer amount;
        BigDecimal quantity;

        public OrderResponseDto(Order order) {
            this.orderSide = order.getOrderSide();
            this.ticker = order.getCoin().getTicker();
            this.amount = order.getAmount();
            this.quantity = new BigDecimal(amount).divide(order.getCoin().getPrice(), 8, RoundingMode.HALF_UP);
        }
    }
}
