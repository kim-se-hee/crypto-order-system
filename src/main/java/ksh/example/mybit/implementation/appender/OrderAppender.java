package ksh.example.mybit.implementation.appender;

import ksh.example.mybit.implementation.definition.OrderCreateDefinition;
import ksh.example.mybit.persistence.mysql.jpa.entity.Order;
import ksh.example.mybit.service.result.OrderId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * @author Theo
 * @since 2025/01/16
 */
@Component
public class OrderAppender {
    @Transactional
    public OrderId append(OrderCreateDefinition definition) {
        definition.validator().checkMemberIsValid(definition.memberId());
        // jpa repository save
        Order order = new Order(
                validator,
                definition.amount(),
                definition.orderSide(),
                definition.orderType(),
                definition.limitPrice()
        );
        return new OrderId(order.getId());
    }
}
