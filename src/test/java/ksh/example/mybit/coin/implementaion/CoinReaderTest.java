package ksh.example.mybit.coin.implementaion;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CoinReaderTest {

    @Autowired
    private CoinReader coinReader;

    @Autowired
    private CoinRepository coinRepository;

    @DisplayName("주어진 id를 이용해 코인을 조회한다")
    @Test
    void readById1(){
        //given
        Coin coin = Coin.builder().build();
        coinRepository.save(coin);

        //when
        Coin findCoin = coinReader.readById(coin.getId());

        //then
        assertThat(findCoin).isEqualTo(coin);
     }

    @DisplayName("id에 해당하는 코인이 없으면 예외가 발생한다")
    @Test
    void readById2(){
        //when //then
        assertThatThrownBy(() -> coinReader.readById(2l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 자산입니다");
    }

    @DisplayName("상장된 자산을 페이징 하여 조회한다")
    @Test
    void readAll1(){
        //given
        Coin coin1 = createCoin("btc");
        Coin coin2 = createCoin("eth");
        Coin coin3 = createCoin("sol");

        coinRepository.saveAll(List.of(coin1, coin2, coin3));

        PageRequest pageRequest = PageRequest.of(0, 2);
        //when
        Page<Coin> coins = coinReader.readAll(pageRequest);

        //then
        assertThat(coins).hasSize(2)
                .extracting(Coin::getName)
                .containsExactlyInAnyOrder("btc", "eth");
     }

     @DisplayName("전체 페이지 수보다 큰 페이지 번호가 주어지면 빈 리스트가 조회된다")
     @Test
     void readAll2(){
         //given
         Coin coin1 = createCoin("btc");
         Coin coin2 = createCoin("eth");
         Coin coin3 = createCoin("sol");

         coinRepository.saveAll(List.of(coin1, coin2, coin3));

         PageRequest pageRequest = PageRequest.of(10, 2);

         //when
         Page<Coin> coins = coinReader.readAll(pageRequest);

         // then
         assertThat(coins).isEmpty();
      }

      private static Coin createCoin(String name){
        return Coin.builder()
                .name(name)
                .build();
      }
}
