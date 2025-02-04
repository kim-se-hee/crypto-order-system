package ksh.example.mybit.order.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenOrderServiceRequest {

    Long memberId;
    Long coinId;

    @Builder
    private OpenOrderServiceRequest(Long memberId, Long coinId) {
        this.memberId = memberId;
        this.coinId = coinId;
    }
}
