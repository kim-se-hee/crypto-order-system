package ksh.example.mybit.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.controller.form.OrderForm;
import ksh.example.mybit.domain.Coin;
import ksh.example.mybit.domain.Member;
import ksh.example.mybit.domain.Order;
import ksh.example.mybit.service.CoinService;
import ksh.example.mybit.service.MemberService;
import ksh.example.mybit.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final CoinService coinService;

    @PostMapping("/order")
    public ResponseEntity<Boolean> orderAdd(@Valid @ModelAttribute OrderForm orderForm) {
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
        orderService.addOrder(order);
        return ResponseEntity.ok(true);
    }
}
