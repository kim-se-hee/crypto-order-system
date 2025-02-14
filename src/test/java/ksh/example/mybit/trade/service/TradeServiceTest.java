package ksh.example.mybit.trade.service;

import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.repository.CoinRepository;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.repository.MemberRepository;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.domain.OrderSide;
import ksh.example.mybit.order.repository.OrderRepository;
import ksh.example.mybit.trade.domain.Trade;
import ksh.example.mybit.trade.repository.TradeRepository;
import ksh.example.mybit.trade.service.dto.response.TradeHistoryListResponse;
import ksh.example.mybit.trade.service.dto.response.TradeHistoryResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static ksh.example.mybit.order.domain.OrderSide.BUY;
import static ksh.example.mybit.order.domain.OrderSide.SELL;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TradeServiceTest {


    /// OrderSide orderSide;
    ///     String ticker;
    ///     LocalDateTime tradeDateTime;
    ///     BigDecimal executedQuantity;
    ///     Integer executedVolume;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TradeService tradeService;

    @DisplayName("회원의 특정 코인 거래 내역을 페이징하여 조회한다")
    @Test
    void getTradeHistory() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.saveAll(List.of(member1, member2));

        Coin coin = new Coin();
        coinRepository.save(coin);

        Order buyOrder1 = createOrder(BUY, member1, coin);
        Order sellOrder1 = createOrder(SELL, member1, coin);
        Order buyOrder2 = createOrder(BUY, member2, coin);
        Order sellOrder2 = createOrder(SELL, member2, coin);

        orderRepository.saveAll(List.of(buyOrder1, sellOrder1, buyOrder2, sellOrder2));

        Trade trade1 = createTrade(buyOrder1, sellOrder2, 10000, BigDecimal.valueOf(2000), LocalDateTime.of(2025, 2, 1, 0 ,0));
        Trade trade2 = createTrade(buyOrder2, sellOrder1, 9000, BigDecimal.valueOf(1000), LocalDateTime.of(2025, 2, 1, 0 ,1));
        Trade trade3 = createTrade(buyOrder1, sellOrder2, 8000, BigDecimal.valueOf(500), LocalDateTime.of(2025, 2, 1, 0 ,2));
        Trade trade4 = createTrade(buyOrder2, sellOrder1, 7000, BigDecimal.valueOf(100), LocalDateTime.of(2025, 2, 1, 0 ,3));
        tradeRepository.saveAll(List.of(trade1, trade2, trade3, trade4));

        PageRequest request = PageRequest.of(0, 3);

        //when
        TradeHistoryListResponse response = tradeService.getTradeHistory(member1.getId(), coin.getId(), request);

        //then
        assertThat(response.getHistories()).hasSize(3)
                .extracting("orderSide", "executedQuantity", "executedVolume")
                .containsExactlyInAnyOrder(
                        tuple(BUY, BigDecimal.valueOf(2000), 10000),
                        tuple(SELL, BigDecimal.valueOf(1000), 9000),
                        tuple(BUY, BigDecimal.valueOf(500), 8000)
                );
        assertThat(response.getHistories())
                .isSortedAccordingTo(Comparator.comparing(TradeHistoryResponse::getTradeDateTime));
     }

    private static Order createOrder(OrderSide orderSide, Member member, Coin coin) {
        return Order.builder()
                .orderSide(orderSide)
                .member(member)
                .coin(coin)
                .build();
    }

    private static Trade createTrade(Order buyOrder, Order sellOrder, int volume, BigDecimal quantity, LocalDateTime createdAt) {
        return Trade.builder()
                .buyOrder(buyOrder)
                .sellOrder(sellOrder)
                .executedVolume(volume)
                .executedQuantity(quantity)
                .createdAt(createdAt)
                .build();
    }
}
