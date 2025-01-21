package ksh.example.mybit.order.service;

import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.dto.request.OpenOrderRequestDto;
import ksh.example.mybit.order.dto.request.OrderCreateRequestDto;
import ksh.example.mybit.order.dto.response.OrderCreateResponseDto;
import ksh.example.mybit.order.implementation.OrderReader;
import ksh.example.mybit.order.implementation.OrderValidator;
import ksh.example.mybit.order.implementation.OrderWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderValidator orderValidator;
    private final OrderReader orderReader;
    private final OrderWriter orderWriter;


    @Transactional
    public OrderCreateResponseDto placeOrder(OrderCreateRequestDto requestDto) {
        orderValidator.checkTimeIntervalFromLatestOrder(requestDto.getMemberId(), requestDto.getCoinId(), requestDto.getOrderSide());

        orderValidator.checkOrderVolumeIsValid(requestDto.getMemberId(), requestDto.getCoinId(), requestDto.getOrderVolume(), requestDto.getOrderSide());

        Order order = orderWriter.create(requestDto);
        return new OrderCreateResponseDto(order.getId());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        orderValidator.checkOrderIsPending(orderId);

        orderWriter.cancel(orderId);
    }

    public List<Order> getOpenOrders(OpenOrderRequestDto requestDto, Pageable pageable) {
        return orderReader.readPendingOrdersBy(requestDto.getMemberId(), requestDto.getCoinId(), pageable);
    }
}
