package ksh.example.mybit.membercoin.dto.request;

import ksh.example.mybit.membercoin.service.dto.request.FundTransferServiceRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FundTransferRequestTest {

    @DisplayName("내용이 같은 서비스 요청 DTO로 변환할 수 있다")
    @Test
    void toServiceRequest() {
        //given
        Long coinId = 1L;
        Long memberId = 2L;
        BigDecimal quantity = new BigDecimal("100.00");

        FundTransferRequest request = new FundTransferRequest();
        request.setCoinId(coinId);
        request.setMemberId(memberId);
        request.setQuantity(quantity);

        //when
        FundTransferServiceRequest serviceRequest = request.toServiceRequest();

        //then
        assertThat(serviceRequest)
                .extracting("memberId", "coinId", "quantity")
                .containsExactly(memberId, coinId, quantity);
     }


}
