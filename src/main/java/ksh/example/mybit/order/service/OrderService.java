package ksh.example.mybit.order.service;

import ksh.example.mybit.order.domain.Order;
import ksh.example.mybit.order.dto.request.OpenOrderRequestDto;
import ksh.example.mybit.order.dto.request.OrderCreateRequestDto;
import ksh.example.mybit.order.service.dto.request.OpenOrderServiceRequest;
import ksh.example.mybit.order.service.dto.request.OrderCreateServiceRequest;
import ksh.example.mybit.order.service.dto.response.OrderCreateResponse;
import ksh.example.mybit.order.service.dto.response.OrderListResponse;
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
    public OrderCreateResponse placeOrder(OrderCreateServiceRequest request) {
        orderValidator.checkTimeIntervalFromLatestOrder(request.getMemberId(), request.getCoinId(), request.getOrderSide());

        orderValidator.checkOrderVolumeIsValid(request.getMemberId(), request.getCoinId(), request.getOrderVolume(), request.getOrderSide());

        Order order = orderWriter.create(request.toEntity(), request.getMemberId(), request.getCoinId());
        return new OrderCreateResponse(order.getId());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        orderValidator.checkOrderIsPending(orderId);

        orderWriter.cancel(orderId);
    }

    public OrderListResponse getOpenOrders(OpenOrderServiceRequest request, Pageable pageable) {
        List<Order> pendingOrders = orderReader.readPendingOrdersBy(request.getMemberId(), request.getCoinId(), pageable);

        return new OrderListResponse(pendingOrders);
    }
}
