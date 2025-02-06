package ksh.example.mybit.membercoin.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.example.mybit.membercoin.service.dto.request.FundTransferServiceRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FundTransferRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private Long coinId;

    @NotNull
    private BigDecimal quantity;

    public FundTransferServiceRequest toServiceRequest() {
        return FundTransferServiceRequest.builder()
                .memberId(memberId)
                .coinId(coinId)
                .quantity(quantity)
                .build();
    }

}
