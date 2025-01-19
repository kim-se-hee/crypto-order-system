package ksh.example.mybit.controller.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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
