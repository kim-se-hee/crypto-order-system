package ksh.example.mybit.membercoin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ksh.example.mybit.membercoin.service.dto.request.InvestmentStaticsServiceRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentStaticsRequest {

    @Positive(message = "회원 id는 양의 정수입니다")
    @NotNull(message = "회원 id는 필수입니다")
    private Long memberId;

    @Positive(message = "코인 id는 양의 정수입니다")
    @NotNull(message = "코인 id는 필수입니다")
    private Long coinId;

    public InvestmentStaticsServiceRequest toServiceRequest() {
        return InvestmentStaticsServiceRequest.builder()
                .memberId(memberId)
                .coinId(coinId)
                .build();
    }

    @Builder
    private InvestmentStaticsRequest(Long memberId, Long coinId) {
        this.memberId = memberId;
        this.coinId = coinId;
    }
}
