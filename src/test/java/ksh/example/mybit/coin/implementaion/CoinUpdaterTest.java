package ksh.example.mybit.coin.implementaion;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.order.repository.OrderRepository;
import ksh.example.mybit.trade.repository.TradeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CoinUpdaterTest {

    @Autowired
    private CoinUpdater coinUpdater;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @DisplayName("주어진 가격으로 코인의 가격을 변경한다")
    @Test
    void test(){
        //given
        Coin coin = Coin.builder()
                .price(BigDecimal.valueOf(950))
                .build();
        coinRepository.save(coin);

        //when
        coinUpdater.updatePrice(coin, BigDecimal.valueOf(1000));

        //then
        assertThat(coin.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
     }

}
