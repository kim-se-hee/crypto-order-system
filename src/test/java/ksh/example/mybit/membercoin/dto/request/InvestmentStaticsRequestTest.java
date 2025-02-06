package ksh.example.mybit.membercoin.dto.request;

import ksh.example.mybit.membercoin.service.dto.request.InvestmentStaticsServiceRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class InvestmentStaticsRequestTest {

    @DisplayName("내용이 같은 서비스 요청 DTO로 변환할 수 있다")
    @Test
    void toServiceRequest() {
        //given
        InvestmentStaticsRequest request = new InvestmentStaticsRequest();
        request.setMemberId(1l);
        request.setCoinId(2l);

        //when
        InvestmentStaticsServiceRequest serviceRequest = request.toServiceRequest();

        //then
        assertThat(serviceRequest)
                .extracting("memberId", "coinId")
                .containsExactly(1l, 2l);
     }

}
