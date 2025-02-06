package ksh.example.mybit.order.dto.request;

import ksh.example.mybit.order.service.dto.request.OpenOrderServiceRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OpenOrderRequestDtoTest {

    @DisplayName("내용이 같은 서비스 요청 DTO로 변환할 수 있다")
    @Test
    void test(){
        //given
        OpenOrderRequestDto request = new OpenOrderRequestDto();
        request.setMemberId(1l);
        request.setCoinId(2l);

        //when
        OpenOrderServiceRequest serviceRequest = request.toServiceRequest();

        //then
        assertThat(serviceRequest)
                .extracting("memberId", "coinId")
                .containsExactly(1l, 2l);
     }

}
