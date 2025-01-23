package ksh.example.mybit.membercoin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositWithdrawForm {

    @NotNull
    private Long memberId;

    @NotNull
    private Long coinId;

    @NotNull
    private BigDecimal quantity;

}
