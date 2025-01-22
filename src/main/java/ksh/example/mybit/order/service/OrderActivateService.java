package ksh.example.mybit.order.service;

import ksh.example.mybit.order.implementation.OrderWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderActivateService {

    private final OrderWriter orderWriter;

    @Transactional
    public void activatePreOrders() {
        orderWriter.activateTriggeredOrders();
    }

}
