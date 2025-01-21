package ksh.example.mybit.order.controller;

import jakarta.validation.Valid;
import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.dto.request.OpenOrderRequestDto;
import ksh.example.mybit.order.dto.request.OrderCreateRequestDto;
import ksh.example.mybit.order.dto.response.OrderCreateResponseDto;
import ksh.example.mybit.order.dto.response.OrderResponseListDto;
import ksh.example.mybit.order.service.OrderService;
import ksh.example.mybit.service.LockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    private final LockService lockService;

    @GetMapping("/order/open")
    public OrderResponseListDto orderOpen(@Valid @RequestBody OpenOrderRequestDto orderRequestDto, Pageable pageable) {
        List<Order> openOrders = orderService.getOpenOrders(orderRequestDto, pageable);

        return new OrderResponseListDto(openOrders);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderCreateResponseDto> orderAdd(@Valid @RequestBody OrderCreateRequestDto requestDto) {
        lockService.tryLock(requestDto.getMemberId());

        OrderCreateResponseDto responseDto;
        try {
            responseDto = orderService.placeOrder(requestDto);
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
