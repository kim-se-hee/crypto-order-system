package ksh.example.mybit.order.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.coin.domain.Coin;
import ksh.example.mybit.coin.service.CoinService;
import ksh.example.mybit.member.domain.Member;
import ksh.example.mybit.member.service.MemberService;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.dto.request.OpenOrderForm;
import ksh.example.mybit.order.dto.request.OrderForm;
import ksh.example.mybit.order.dto.response.OrdersResponseDto;
import ksh.example.mybit.order.service.OrderService;
import ksh.example.mybit.service.LockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final CoinService coinService;

    private final LockService lockService;

    @GetMapping("/order/open")
    public OrdersResponseDto orderOpen(@Valid @ModelAttribute OpenOrderForm openOrderForm, Pageable pageable) {
        List<Order> openOrders = orderService.getOpenOrders(openOrderForm.getMemberId(), openOrderForm.getCoinId(), pageable);

        return new OrdersResponseDto(openOrders);
    }

    @PostMapping("/order")
    public ResponseEntity<Boolean> orderAdd(@Valid @ModelAttribute OrderForm orderForm) {
        lockService.tryLock(orderForm.getMemberId());

        try {
            Member member = memberService.findMember(orderForm.getMemberId());
            Coin coin = coinService.findCoin(orderForm.getCoinId());

            Order order = new Order(
                    orderForm.getOrderVolume(),
                    orderForm.getOrderSide(),
                    orderForm.getOrderType(),
                    orderForm.getLimitPrice(),
                    member,
                    coin
            );


            orderService.placeOrder(order);
        } finally {
            lockService.unLock(orderForm.getMemberId());
        }


        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Boolean> orderDelete(@PathVariable Long id) {
        orderService.cancelOrder(id);

        return ResponseEntity.ok(true);
    }
}
