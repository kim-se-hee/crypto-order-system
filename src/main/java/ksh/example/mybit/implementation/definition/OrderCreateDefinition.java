package ksh.example.mybit.implementation.definition;

import ksh.example.mybit.implementation.Validator;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderSide;
import ksh.example.mybit.persistence.mysql.jpa.entity.type.OrderType;

import java.math.BigDecimal;

/**
 * @author Theo
 * @since 2025/01/16
 */
public record OrderCreateDefinition(
        Validator validator,
        Integer amount,
        OrderSide orderSide,
        OrderType orderType,
        BigDecimal limitPrice
) {
}
