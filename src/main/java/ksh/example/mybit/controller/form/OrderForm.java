package ksh.example.mybit.controller.form;

import jakarta.validation.constraints.NotNull;
import ksh.example.mybit.domain.OrderSide;
import ksh.example.mybit.domain.OrderType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class OrderForm {

    @NotNull
    private Long userId;

    @NotNull
    private Long coinId;

    @NotNull
    @Range(min = 5000, max = 1000000000)
    private Integer orderAmount;

    @NotNull
    private OrderSide orderSide;

    @NotNull
    private OrderType orderType;

    private Integer limitPrice;

}