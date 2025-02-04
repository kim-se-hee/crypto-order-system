package ksh.example.mybit.membercoin.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestmentStaticsServiceRequest {

    private Long memberId;

    private Long coinId;

    @Builder
    private InvestmentStaticsServiceRequest(Long memberId, Long coinId) {
        this.memberId = memberId;
        this.coinId = coinId;
    }
}
