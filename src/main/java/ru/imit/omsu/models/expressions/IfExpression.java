package ru.imit.omsu.models.expressions;

import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.*;
import java.util.regex.Matcher;

public class IfExpression extends Expression {

    private Expression condition;
    private Expression expressionTrue;
    private Expression expressionFalse;

    public IfExpression(Expression condition, Expression expressionTrue, Expression expressionFalse) {
        this.condition = condition;
        this.expressionTrue = expressionTrue;
        this.expressionFalse = expressionFalse;
    }

    public IfExpression(String ifExpression, Integer line) throws InterpreterException {
        Matcher matcher = IF_EXPRESSION_PATTERN.matcher(ifExpression);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        condition = Expression.getExpression(matcher.group(1), line);
        expressionTrue = Expression.getExpression(matcher.group(2), line);
        expressionFalse = Expression.getExpression(matcher.group(3), line);
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getExpressionTrue() {
        return expressionTrue;
    }

    public Expression getExpressionFalse() {
        return expressionFalse;
    }

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws InterpreterException {
        return condition.getValueWithParams(idToValue) != 0
                ? expressionTrue.getValueWithParams(idToValue)
                : expressionFalse.getValueWithParams(idToValue);
    }

    @Override
    public String toString() {
        return "[" + condition + "]?{" +
                 expressionTrue +
                "}:{" + expressionFalse +
                "}";
    }
}
