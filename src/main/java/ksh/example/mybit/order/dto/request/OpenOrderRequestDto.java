package ksh.example.mybit.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenOrderRequestDto {

    @NotNull
    Long memberId;
    @NotNull
    Long coinId;

}
