package ksh.example.mybit.order.service.dto.request;

import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderCreateServiceRequest {

    private Long memberId;
    private Long coinId;
    private Integer orderVolume;
    private OrderSide orderSide;
    private OrderType orderType;
    private BigDecimal limitPrice;

    @Builder
    private OrderCreateServiceRequest(Long memberId, Long coinId, Integer orderVolume, OrderSide orderSide, OrderType orderType, BigDecimal limitPrice) {
        this.memberId = memberId;
        this.coinId = coinId;
        this.orderVolume = orderVolume;
        this.orderSide = orderSide;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
    }
}
