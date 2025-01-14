package ksh.example.mybit.controller.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenOrderForm {

    @NotNull
    Long memberId;
    @NotNull
    Long coinId;

}
