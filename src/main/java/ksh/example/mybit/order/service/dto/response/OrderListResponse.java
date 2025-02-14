package ksh.example.mybit.order.service.dto.response;

import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@Setter
public class OrderListResponse {

    List<OrderResponseDto> orderResponses;

    public OrderListResponse(List<Order> orders) {
        this.orderResponses = orders.stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    @Getter
    @Setter
    static class OrderResponseDto {

        Long id;
        OrderSide orderSide;
        String ticker;
        Integer volume;
        BigDecimal quantity;

        public OrderResponseDto(Order order) {
            this.id = order.getId();
            this.orderSide = order.getOrderSide();
            this.ticker = order.getCoin().getTicker();
            this.volume = order.getVolume();
            this.quantity = order.getQuantity();
        }
    }
}
