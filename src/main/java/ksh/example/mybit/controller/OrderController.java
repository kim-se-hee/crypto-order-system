package ksh.example.mybit.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.controller.form.OrderForm;
import ksh.example.mybit.persistence.mysql.jpa.entity.Coin;
import ksh.example.mybit.persistence.mysql.jpa.entity.Member;
import ksh.example.mybit.persistence.mysql.jpa.entity.Order;
import ksh.example.mybit.service.CoinService;
import ksh.example.mybit.service.LockService;
import ksh.example.mybit.service.MemberService;
import ksh.example.mybit.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final LockService lockService;

    // controller -> service -> implementation -> repository(jpa) / entity(jpa)
    // 1. entity 얘는 무엇을 의존하고 있는가? -> 현재는 없음.
    // 2. entity 를 의존하고 있는 것은 무엇인가?
    @PostMapping("/order")
    public ResponseEntity<Boolean> orderAdd(@Valid @ModelAttribute OrderForm orderForm) {
        lockService.tryLock(orderForm.getMemberId());

        try{
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
