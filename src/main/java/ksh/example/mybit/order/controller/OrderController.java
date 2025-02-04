package ksh.example.mybit.order.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.order.dto.request.OpenOrderRequestDto;
import ksh.example.mybit.order.dto.request.OrderCreateRequestDto;
import ksh.example.mybit.order.service.dto.response.OrderCreateResponse;
import ksh.example.mybit.order.service.dto.response.OrderListResponse;
import ksh.example.mybit.order.service.OrderService;
import ksh.example.mybit.global.util.lock.LockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    private final LockService lockService;

    @GetMapping("/order/open")
    public ResponseEntity<OrderListResponse> orderOpen(@Valid @RequestBody OpenOrderRequestDto orderRequestDto, Pageable pageable) {
        OrderListResponse openOrders = orderService.getOpenOrders(orderRequestDto.toServiceRequest(), pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(openOrders);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderCreateResponse> orderAdd(@Valid @RequestBody OrderCreateRequestDto requestDto) {
        lockService.tryLock(requestDto.getMemberId());

        OrderCreateResponse responseDto;
        try {
            responseDto = orderService.placeOrder(requestDto.toServiceRequest());
        } finally {
            lockService.unLock(requestDto.getMemberId());
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Boolean> orderDelete(@PathVariable Long id) {
        orderService.cancelOrder(id);

        return ResponseEntity.ok(true);
    }
}
