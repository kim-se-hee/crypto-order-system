package ksh.example.mybit.order.implementation;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static ksh.example.mybit.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderWriterTest {

    @Autowired
    private OrderWriter orderWriter;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoinRepository coinRepository;

    @DisplayName("주문을 생성한다")
    @Test
    void create1(){
        //given
        Member member = new Member();
        memberRepository.save(member);

        Coin coin = new Coin();
        coinRepository.save(coin);

        Order order = Order.builder()
                .member(member)
                .coin(coin)
                .build();

        //when
        Order createdOrder = orderWriter.create(order, member.getId(), coin.getId());

        //then
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder).extracting("member", "coin")
                .containsExactly(member, coin);
     }

    @DisplayName("요청한 코인이 없다면 주문이 생성되지 않고 예외가 발생한다.")
    @Test
    void create2(){
        //given
        Member member = new Member();
        memberRepository.save(member);

        Order order = Order.builder()
                .member(member)
                .build();

        //when //then
        assertThatThrownBy(() -> orderWriter.create(order, member.getId(), 1l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 자산입니다");
    }

    @DisplayName("요청한 회원이 없다면 주문이 생성되지 않고 예외가 발생한다.")
    @Test
    void create3(){
        //given
        Coin coin = new Coin();
        coinRepository.save(coin);

        Order order = Order.builder()
                .coin(coin)
                .build();

        //when //then
        assertThatThrownBy(() -> orderWriter.create(order, 1l, coin.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 회원입니다.");
    }

    @DisplayName("미체결 주문을 취소 상태로 바꾼다")
    @Test
    void cancel1(){
        //given
        Order order = Order.builder()
                .orderStatus(PENDING)
                .build();

        //when
        orderWriter.cancel(order);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(CANCELED);
     }

    @DisplayName("미체결 주문이 아니라면 주문 상태가 변하지 않는다")
    @ParameterizedTest
    @CsvSource({"FINISHED", "CANCELED"})
    void cancel2(OrderStatus orderStatus){
        //given
        Order order = Order.builder()
                .orderStatus(orderStatus)
                .build();

        //when
        orderWriter.cancel(order);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

}
