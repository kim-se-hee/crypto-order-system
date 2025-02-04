package ksh.example.mybit.membercoin.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FundTransferServiceRequest {

    private Long memberId;

    private Long coinId;

    private BigDecimal quantity;

    @Builder
    private FundTransferServiceRequest(Long memberId, Long coinId, BigDecimal quantity) {
        this.memberId = memberId;
        this.coinId = coinId;
        this.quantity = quantity;
    }
}
