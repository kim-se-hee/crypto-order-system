package ksh.example.mybit.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BigDecimalCalculateUtilTest {

    @DisplayName("첫번째 피연산자를 설정한다")
    @Test
    void init(){

        //when
        BigDecimalCalculateUtil util = BigDecimalCalculateUtil.init(1000);

        //then
        assertThat(util.getValue()).isEqualTo(BigDecimal.valueOf(1000));

    }

    private static Stream<Arguments> numberProvider(){
        return Stream.of(
                Arguments.of(1),
                Arguments.of(0.1),
                Arguments.of(3000000000l)
        );
    }
    @DisplayName("주어진 숫자 타입의 값을 BigDecimal로 변환한다")
    @ParameterizedTest
    @MethodSource("numberProvider")
    void toBigDecimal(Number value){
        //when
        BigDecimal bigDecimal = BigDecimalCalculateUtil.toBigDecimal(value);

        //then
        assertThat(bigDecimal).isEqualTo(new BigDecimal(value.toString()));
     }

     @DisplayName("숫자가 아닌 값이 주어지면 예외가 발생한다")
     @Test
     void nonNumberToBigDecimal(){
         //given
         String s = "test";

         //when //then
         assertThatThrownBy(() -> BigDecimalCalculateUtil.toBigDecimal(s))
                 .isInstanceOf(NumberFormatException.class);

      }

      @DisplayName("주어진 수를 저장된 중간 계산 결과에 더한다")
      @Test
      void add(){
          //given
          BigDecimalCalculateUtil util = BigDecimalCalculateUtil.init(1000);

          //when
          BigDecimal result = util.add(0.1).getValue();

          //then
          assertThat(result).isEqualTo(new BigDecimal("1000.1"));
       }

    @DisplayName("주어진 수를 저장된 중간 계산 결과에서 뺀다")
    @Test
    void subtract(){
        //given
        BigDecimalCalculateUtil util = BigDecimalCalculateUtil.init(1000);

        //when
        BigDecimal result = util.subtract(0.1).getValue();

        //then
        assertThat(result).isEqualTo(new BigDecimal("999.9"));
    }

    @DisplayName("주어진 수를 저장된 중간 계산 결과에 곱한다")
    @Test
    void multiply(){
        //given
        BigDecimalCalculateUtil util = BigDecimalCalculateUtil.init(1000);

        //when
        BigDecimal result = util.multiply(0.1).getValue();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(100.0));
    }

    @DisplayName("주어진 수를 저장된 중간 계산 결과에서 나눈 후 조건에 맞게 라운딩 한다")
    @Test
    void divide(){
        //given
        BigDecimalCalculateUtil util = BigDecimalCalculateUtil.init(1000);

        //when
        BigDecimal result = util.divide(7, 2, RoundingMode.HALF_UP).getValue();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(142.86));
     }

    @DisplayName("0으로 나누면 예외가 발생한다")
    @Test
    void divideByZero(){
        //given
        BigDecimalCalculateUtil util = BigDecimalCalculateUtil.init(1000);

        //when //then
        assertThatThrownBy(() -> util.divide(0, 2, RoundingMode.HALF_UP))
                .isInstanceOf(ArithmeticException.class);
    }

}
