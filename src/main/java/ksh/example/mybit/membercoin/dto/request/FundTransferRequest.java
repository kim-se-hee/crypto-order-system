package ksh.example.mybit.membercoin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ksh.example.mybit.membercoin.service.dto.request.FundTransferServiceRequest;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundTransferRequest {

    @NotNull(message = "회원 id는 필수입니다")
    @Positive(message = "회원 id는 양의 정수입니다")
    private Long memberId;

    @NotNull(message = "코인 id는 필수입니다")
    @Positive(message = "코인 id는 양의 정수입니다")
    private Long coinId;

    @NotNull(message = "수량은 필수입니다")
    @Positive(message = "수량은 양수입니다")
    private BigDecimal quantity;

    public FundTransferServiceRequest toServiceRequest() {
        return FundTransferServiceRequest.builder()
                .memberId(memberId)
                .coinId(coinId)
                .quantity(quantity)
                .build();
    }

    @Builder
    private FundTransferRequest(Long memberId, Long coinId, BigDecimal quantity) {
        this.memberId = memberId;
        this.coinId = coinId;
        this.quantity = quantity;
    }
}
