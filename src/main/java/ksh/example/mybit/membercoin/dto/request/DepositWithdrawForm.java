package ksh.example.mybit.membercoin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositWithdrawForm {

    @NotNull
    private Long memberId;

    @NotNull
    private Long coinId;

    @NotNull
    private Integer volume;

}
