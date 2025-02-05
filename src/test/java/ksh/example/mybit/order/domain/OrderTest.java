package ksh.example.mybit.order.domain;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("주어진 수량만큼 주문 양을 감소시킬 수 있다.")
    @Test
    void decreaseVolume(){
        //given
        Order order = Order.builder()
                .volume(10000)
                .build();

        Integer decreaseVolume = 10000;

        //when
        order.updateVolume(decreaseVolume);

        //then
        assertThat(order.getVolume()).isEqualTo(0);
     }

    @DisplayName("남아있는 주문 양보다 많이 감소시키려 하면 예외가 발생한다.")
    @Test
    void decreaseLargerThanVolume(){
        //given
        Order order = Order.builder()
                .volume(10000)
                .build();

        Integer decreaseVolume = 10001;

        //when // then
        assertThatThrownBy(() -> order.updateVolume(decreaseVolume))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("남아있는 주문 양이 부족합니다.");
    }


    @DisplayName("현재 주문이 처리 완료 되었는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"0,true", "1,false"})
    void isFinished(int volume, boolean result){
        //given
        Order order = Order.builder()
                .volume(volume)
                .build();

        //when
        boolean finished = order.isFinished();

        //then
        assertThat(finished).isEqualTo(result);
     }

     @DisplayName("주문을 처리 완료 상태로 변경한다.")
     @Test
     void finish(){
         //given
         Order order = Order.builder()
                 .orderStatus(OrderStatus.PENDING)
                 .build();

         //when
         order.finish();

         //then
         assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.FINISHED);

      }

    @DisplayName("주문을 취소 상태로 변경한다.")
    @Test
    void cancel(){
        //given
        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .build();

        //when
        order.cancel();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);

    }

    @DisplayName("주문을 회원 및 코인 정보와 연관시킨다.")
    @Test
    void test(){
        //given
        Member member = Member.builder().build();
        Coin coin = Coin.builder().build();
        Order order = Order.builder().build();

        //when
        order.setMemberAndCoin(member, coin);

        //then
        assertThat(order)
                .extracting("member", "coin")
                .contains(member, coin);

     }

}
