package ksh.example.mybit.service.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Theo
 * @since 2025/01/16
 */
class OrderIdTest {

    @Test
    void testOrderId() {
        assertThrows(IllegalArgumentException.class, () -> new OrderId(null));
        assertThrows(IllegalArgumentException.class, () -> new OrderId(0L));
        assertThrows(IllegalArgumentException.class, () -> new OrderId(-1L));
    }
}