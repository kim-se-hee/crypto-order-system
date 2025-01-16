package ksh.example.mybit.service;

import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderSide;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderType;
import ksh.example.mybit.service.result.OrderId;

import java.math.BigDecimal;

/**
 * @author Theo
 * @since 2025/01/16
 */
public interface OrderCreateBuy {
    OrderId execute(OrderCreateBuyCommand command);

     record OrderCreateBuyCommand(
            Integer amount,
            OrderSide orderSide,
            OrderType orderType,
            BigDecimal limitPrice,
            long coinId,
            long memberId
    ) {
    }
}
