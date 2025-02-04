package ksh.example.mybit.order.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.example.mybit.order.service.dto.request.OpenOrderServiceRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenOrderRequestDto {

    @NotNull
    Long memberId;
    @NotNull
    Long coinId;

    public OpenOrderServiceRequest toServiceRequest() {
        return OpenOrderServiceRequest.builder()
                .memberId(memberId)
                .coinId(coinId)
                .build();
    }

}
