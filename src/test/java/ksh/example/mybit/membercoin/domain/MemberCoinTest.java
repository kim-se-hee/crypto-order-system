package ksh.example.mybit.membercoin.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberCoinTest {

    @DisplayName("주어진 수량만큼 현재 수량을 증가시킬 수 있다")
    @Test
    void increaseQuantity() {
        //given
        MemberCoin memberCoin = MemberCoin.builder()
                .quantity(BigDecimal.valueOf(10000))
                .build();
        BigDecimal increaseAmount = BigDecimal.valueOf(2000);

        //when
        memberCoin.increaseQuantity(increaseAmount);

        //then
        assertThat(memberCoin.getQuantity()).isEqualTo(BigDecimal.valueOf(12000));
     }

    @DisplayName("주어진 수량만큼 현재 수량을 감소시킬 수 있다")
    @Test
    void decreaseQuantity1() {
        //given
        MemberCoin memberCoin = MemberCoin.builder()
                .quantity(BigDecimal.valueOf(10000))
                .build();
        BigDecimal decreaseAmount = BigDecimal.valueOf(10000);

        //when
        memberCoin.decreaseQuantity(decreaseAmount);

        //then
        assertThat(memberCoin.getQuantity()).isEqualTo(BigDecimal.valueOf(0));
    }

    @DisplayName("현재 보유 수량보다 많이 감소시키려 하면 예외가 발생한다")
    @Test
    void decreaseQuantity2() {
        //given
        MemberCoin memberCoin = MemberCoin.builder()
                .quantity(BigDecimal.valueOf(10000))
                .build();
        BigDecimal decreaseAmount = BigDecimal.valueOf(12000);

        //when //then
        assertThatThrownBy(() -> memberCoin.decreaseQuantity(decreaseAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 보유 수량이 부족합니다.");
    }

    @DisplayName("주어진 가격으로 평단가를 변경할 수 있다")
    @Test
    void updateAveragePrice(){
        //given
        MemberCoin memberCoin = MemberCoin.builder()
                .averagePrice(BigDecimal.valueOf(10000))
                .build();

        BigDecimal targetPrice = BigDecimal.valueOf(0);

        //when
        memberCoin.updateAveragePrice(targetPrice);

        //then
        assertThat(memberCoin.getAveragePrice()).isEqualTo(BigDecimal.valueOf(0));
     }

    @DisplayName("평단가를 0보다 작은 가격으로 변경하려 하면 예외가 발생한다.")
    @Test
    void updateAveragePriceLessThanZero(){
        //given
        MemberCoin memberCoin = MemberCoin.builder()
                .averagePrice(BigDecimal.valueOf(10000))
                .build();

        BigDecimal targetPrice = BigDecimal.valueOf(-0.001);

        //when //then
        assertThatThrownBy(() -> memberCoin.updateAveragePrice(targetPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("평단가는 음수가 될 수 없습니다.");
    }

}
