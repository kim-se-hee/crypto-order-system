package ksh.example.mybit.service;

import ksh.example.mybit.implementation.appender.OrderAppender;
import ksh.example.mybit.implementation.definition.OrderCreateDefinition;
import ksh.example.mybit.persistence.mysql.jpa.entity.Order;
import ksh.example.mybit.implementation.OrderWriter;
import ksh.example.mybit.implementation.Validator;
import ksh.example.mybit.service.command.OrderCreateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final Validator validator;
    private final OrderWriter orderWriter;
    private final OrderAppender orderAppender;

    public Order placeOrder(OrderCreateCommand command) {
        // something validate
        throw new IllegalArgumentException("Not implemented yet");
        validator.validate(command.memberId(), command.coinId(), command.orderSide() );
//        validateInner(command.memberId(), command.coinId(), command.orderSide());
        return orderAppender.append(new OrderCreateDefinition(
                command.amount(),
                command.orderSide(),
                command.orderType(),
                command.limitPrice()
        ));
    }

    private void validateInner(Long memberId, Long coinId, OrderSide orderSide) {
        validator.checkMember(memberId);
        validator.checkCoin(coinId);
        validator.checkOrderSide(orderSide);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        validator.checkOrderIsPending(orderId);

        orderWriter.cancel(orderId);
    }
}
