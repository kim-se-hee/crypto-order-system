package ksh.example.mybit.global.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalCalculateUtil {

    @FunctionalInterface
    public interface BigDecimalOperator {
        BigDecimal operate(BigDecimal a, BigDecimal b);
    }

    private BigDecimal value;

    public BigDecimalCalculateUtil(BigDecimal initialValue) {
        this.value = initialValue;
    }

    public static <T> BigDecimal toBigDecimal(T value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        return new BigDecimal(value.toString());
    }

    public static <T> BigDecimalCalculateUtil init(T initialValue) {
        return new BigDecimalCalculateUtil(toBigDecimal(initialValue));
    }

    public <T> BigDecimalCalculateUtil add(T operand) {
        return calculate(BigDecimal::add, operand);
    }

    public <T> BigDecimalCalculateUtil subtract(T operand) {
        return calculate(BigDecimal::subtract, operand);
    }

    public <T> BigDecimalCalculateUtil multiply(T operand) {
        return calculate(BigDecimal::multiply, operand);
    }

    public <T> BigDecimalCalculateUtil divide(T operand, int scale) {
        return calculate((a, b) -> a.divide(b, scale, RoundingMode.HALF_UP), operand);
    }

    public BigDecimal getValue() {
        return this.value;
    }

    private <T> BigDecimalCalculateUtil calculate(BigDecimalOperator operation, T operand) {
        BigDecimal operandValue = toBigDecimal(operand);
        this.value = operation.operate(this.value, operandValue);
        return this;
    }

}
