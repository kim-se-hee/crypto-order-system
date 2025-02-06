package ksh.example.mybit.order.dto.request;

import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.domain.OrderType;
import ksh.example.mybit.order.service.dto.request.OrderCreateServiceRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderCreateRequestDtoTest {

    @DisplayName("내용이 같은 서비스 요청 DTO로 변환할 수 있다")
    @Test
    void toServiceRequestDto() {
        //given
        OrderCreateRequestDto request = new OrderCreateRequestDto();
        request.setMemberId(1l);
        request.setCoinId(2l);
        request.setOrderSide(OrderSide.SELL);
        request.setOrderType(OrderType.LIMIT);
        request.setOrderVolume(1000);
        request.setLimitPrice(BigDecimal.valueOf(2000));

        //when
        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();

        //then
        assertThat(serviceRequest)
                .extracting("memberId", "coinId", "orderSide", "orderType", "orderVolume", "limitPrice")
                .containsExactly(1l, 2l, OrderSide.SELL, OrderType.LIMIT, 1000, BigDecimal.valueOf(2000));
     }
}
