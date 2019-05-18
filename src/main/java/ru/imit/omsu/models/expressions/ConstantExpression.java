package ru.imit.omsu.models.expressions;

import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;

import java.util.*;

public class ConstantExpression extends Expression {

    private int number;
    private Integer line;

    public ConstantExpression(String constantExpression, Integer line) throws GrammarException {
        if (!CONSTANT_EXPRESSION_PATTERN.matcher(constantExpression).find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        if (constantExpression.startsWith("-")) {
            number = -Integer.parseInt(constantExpression.substring(1));
        } else {
            number = Integer.parseInt(constantExpression);
        }
        this.line = line;
    }

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws GrammarException {
        return number;
    }

    @Override
    public Integer getLine() {
        return line;
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
