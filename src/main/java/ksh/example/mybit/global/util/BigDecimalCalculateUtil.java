package ksh.example.mybit.global.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalCalculateUtil {

    private BigDecimal value;

    private BigDecimalCalculateUtil(BigDecimal initialValue) {
        this.value = initialValue;
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

    public <T> BigDecimalCalculateUtil divide(T operand, int scale, RoundingMode roundingMode) {
        return calculate((a, b) -> a.divide(b, scale, roundingMode), operand);
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

    public BigDecimal getValue() {
        return this.value;
    }

    @FunctionalInterface
    private interface BigDecimalOperator {
        BigDecimal operate(BigDecimal a, BigDecimal b);
    }

    private <T> BigDecimalCalculateUtil calculate(BigDecimalOperator operation, T operand) {
        BigDecimal operandValue = toBigDecimal(operand);
        this.value = operation.operate(this.value, operandValue);
        return this;
    }

}
