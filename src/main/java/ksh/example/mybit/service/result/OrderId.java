package ksh.example.mybit.service.result;

/**
 * @author Theo
 * @since 2025/01/16
 */
// 보통은 별도의 value class 를 생성
public record OrderId(
        Long orderId
) {
    public OrderId {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId must not be null");
        }
        if (orderId <= 0) {
            throw new IllegalArgumentException("orderId must be greater than 0");
        }
    }
}
