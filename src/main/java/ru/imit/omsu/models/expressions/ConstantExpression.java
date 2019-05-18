package ru.imit.omsu.models.expressions;

import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.*;

public class ConstantExpression extends Expression {

    private int number;

    public ConstantExpression(String constantExpression) throws InterpreterException {
        if (!CONSTANT_EXPRESSION_PATTERN.matcher(constantExpression).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        if (constantExpression.startsWith("-")) {
            number = -Integer.parseInt(constantExpression.substring(1));
        } else {
            number = Integer.parseInt(constantExpression);
        }
    }

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws InterpreterException {
        return number;
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
