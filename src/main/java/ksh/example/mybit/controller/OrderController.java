package ksh.example.mybit.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.controller.dto.OrdersResponseDto;
import ksh.example.mybit.controller.form.OpenOrderForm;
import ksh.example.mybit.controller.form.OrderForm;
import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.service.CoinService;
import ksh.example.mybit.service.LockService;
import ksh.example.mybit.service.MemberService;
import ksh.example.mybit.service.OrderService;
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

        try{
            Member member = memberService.findMember(orderForm.getMemberId());
            Coin coin = coinService.findCoin(orderForm.getCoinId());

            Order order = new Order(
                    orderForm.getOrderAmount(),
                    orderForm.getOrderSide(),
                    orderForm.getOrderType(),
                    orderForm.getLimitPrice(),
                    member,
                    coin
            );


            orderService.placeOrder(order);
        }finally{
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
