package ksh.example.mybit.order.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderCreateRequestDto {

    @NotNull
    private Long memberId;

    @NotNull
    private Long coinId;

    @NotNull
    @Range(min = 5000, max = 1000000000)
    private Integer orderVolume;

    @NotNull
    private OrderSide orderSide;

    @NotNull
    private OrderType orderType;

    private BigDecimal limitPrice;

}
