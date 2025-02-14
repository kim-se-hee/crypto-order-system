package ksh.example.mybit.trade.domain;

import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TradeTest {

    @DisplayName("회원 id를 기준으로 거래에서 회원의 주문 방향 반환한다")
    @Test
    void getTradeOrderSideFor() {
        //given
        Member buyer = Member.builder()
                .id(1l)
                .build();

        Member seller = Member.builder()
                .id(2l)
                .build();

        Order buyOrder = Order.builder()
                .member(buyer)
                .build();

        Order sellOrder = Order.builder()
                .member(seller)
                .build();

        Trade trade = Trade.builder()
                .buyOrder(buyOrder)
                .sellOrder(sellOrder)
                .build();

        //when
        OrderSide orderSide = trade.getTradeOrderSideFor(1l);

        //then
        assertThat(orderSide).isEqualTo(OrderSide.BUY);
     }

}
