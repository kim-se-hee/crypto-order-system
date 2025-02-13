package ksh.example.mybit.coin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CoinTest {

    @DisplayName("현재가를 지정한 금액으로 업데이트 할 수 있다")
    @Test
    void updatePrice() {
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(10000))
                .build();
        BigDecimal price = BigDecimal.valueOf(90000);

        //when
        coin.updatePrice(price);

        //then
        assertThat(coin.getPrice()).isEqualTo(price);
    }

    @DisplayName("전일 대비 상승률을 계산한다")
    @Test
    void getChangeRate() {
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(12000))
                .closingPrice(BigDecimal.valueOf(10000))
                .build();

        //when
        BigDecimal changeRate = coin.getChangeRate();

        //then
        assertThat(changeRate).isEqualTo(new BigDecimal("20.00"));
    }
}
