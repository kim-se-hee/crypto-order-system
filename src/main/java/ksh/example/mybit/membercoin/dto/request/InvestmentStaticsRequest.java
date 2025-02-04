package ksh.example.mybit.membercoin.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.example.mybit.membercoin.service.dto.request.InvestmentStaticsServiceRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestmentStaticsRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private Long coinId;

    public InvestmentStaticsServiceRequest toServiceRequest() {
        return InvestmentStaticsServiceRequest.builder()
                .memberId(memberId)
                .coinId(coinId)
                .build();
    }
}
