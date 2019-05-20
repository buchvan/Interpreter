package ru.imit.omsu.models.expressions;

import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.*;

public class ConstantExpression extends Expression {

    private int number;

    private static void checkOfStringConstantExpression(String constantExpression) throws InterpreterException {
        if (!CONSTANT_EXPRESSION_PATTERN.matcher(constantExpression).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
    }

    public ConstantExpression(String constantExpression) throws InterpreterException {
        checkOfStringConstantExpression(constantExpression);
        if (constantExpression.startsWith("-")) {
            number = -Integer.parseInt(constantExpression.substring(1));
        } else {
            number = Integer.parseInt(constantExpression);
        }
    }

    @Override
    public int getValueByParams(Map<String, Integer> idToValue) throws InterpreterException {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstantExpression)) return false;
        ConstantExpression that = (ConstantExpression) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
