package ksh.example.mybit.membercoin.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.membercoin.domain.MemberCoin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PortfolioAnalyzerTest {

    @DisplayName("회원이 보유한 자산의 원화 가치를 계산한다")
    @Test
    void calculateBalance() {
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(1.24))
                .build();

        MemberCoin memberCoin = MemberCoin.builder()
                .coin(coin)
                .quantity(BigDecimal.valueOf(62.285))
                .build();

        //when
        double balance = PortfolioAnalyzer.calculateBalance(memberCoin);

        //then
        assertThat(balance).isEqualTo(77.2334);
     }

     @DisplayName("회원이 보유한 자산의 현재 수익률을 계산한다")
     @Test
     void calculateROI(){
         //given
         Coin coin = Coin.builder()
                 .price(BigDecimal.valueOf(1.24))
                 .build();

         MemberCoin memberCoin = MemberCoin.builder()
                 .coin(coin)
                 .quantity(BigDecimal.valueOf(62.285))
                 .averagePrice(BigDecimal.valueOf(1.17))
                 .build();


         //when
         double roi = PortfolioAnalyzer.calculateROI(memberCoin);

         //then
         assertThat(roi).isEqualTo(5.98);
      }

      @DisplayName("회원이 보유한 자산의 평단가를 계산한다")
      @Test
      void calculateAveragePrice() {
          //given
          MemberCoin memberCoin = MemberCoin.builder()
                  .quantity(BigDecimal.valueOf(62.285))
                  .averagePrice(BigDecimal.valueOf(1.17))
                  .build();

          BigDecimal executedPrice = BigDecimal.valueOf(1.24);
          BigDecimal executedQuantity = BigDecimal.valueOf(115.75155);

          //when
          BigDecimal bigDecimal = PortfolioAnalyzer.calculateAveragePrice(memberCoin, executedPrice, executedQuantity);

          //then
          assertThat(bigDecimal).isEqualTo(BigDecimal.valueOf(1.2155));
       }
}
