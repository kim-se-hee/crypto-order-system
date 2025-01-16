package ksh.example.mybit.service.command;

import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderSide;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderType;

import java.math.BigDecimal;

/**
 * @author Theo
 * @since 2025/01/16
 */
public record OrderCreateCommand(
        Integer amount,
        OrderSide orderSide,
        OrderType orderType,
        BigDecimal limitPrice,
        long coinId,
        long memberId
) {
}
