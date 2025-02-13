package ksh.example.mybit.coin.service;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.coin.service.dto.response.CoinListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinRepository coinRepository;

    @DisplayName("상장된 코인의 시세 정보를 페이징하여 조회할 수 있다")
    @Test
    void getListedCoins(){
        //given
        Coin coin1 = Coin.builder()
                .name("btc")
                .price(BigDecimal.valueOf(12000))
                .closingPrice(BigDecimal.valueOf(10000))
                .build();

        Coin coin2 = Coin.builder()
                .name("eth")
                .price(BigDecimal.valueOf(1100))
                .closingPrice(BigDecimal.valueOf(1000))
                .build();

        Coin coin3 = Coin.builder()
                .name("sol")
                .price(BigDecimal.valueOf(105))
                .closingPrice(BigDecimal.valueOf(100))
                .build();

        coinRepository.saveAll(List.of(coin1, coin2, coin3));
        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        CoinListResponse response = coinService.getListedCoins(pageRequest);

        //then
        assertThat(response)
                .extracting("pageNum", "totalPages", "hasNext")
                .containsExactly(0, 2, true);

        assertThat(response.getCoinList())
                .hasSize(2)
                .extracting("name", "changeRate")
                .containsExactly(
                        tuple("btc", new BigDecimal("20.00")),
                        tuple("eth", new BigDecimal("10.00"))
                );
     }

}
