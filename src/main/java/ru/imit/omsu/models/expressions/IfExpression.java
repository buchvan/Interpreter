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

    private static Matcher checkOfStringIfExpression(String ifExpression) throws InterpreterException {
        Matcher matcher = IF_EXPRESSION_PATTERN.matcher(ifExpression);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        return matcher;
    }

    public IfExpression(String ifExpression) throws InterpreterException {
        Matcher matcher = checkOfStringIfExpression(ifExpression);
        condition = Expression.getExpression(matcher.group(1));
        expressionTrue = Expression.getExpression(matcher.group(2));
        expressionFalse = Expression.getExpression(matcher.group(3));
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
    public int getValueByParams(Map<String, Integer> idToValue) throws InterpreterException {
        return condition.getValueByParams(idToValue) != 0
                ? expressionTrue.getValueByParams(idToValue)
                : expressionFalse.getValueByParams(idToValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IfExpression)) return false;
        IfExpression that = (IfExpression) o;
        return Objects.equals(getCondition(), that.getCondition()) &&
                Objects.equals(getExpressionTrue(), that.getExpressionTrue()) &&
                Objects.equals(getExpressionFalse(), that.getExpressionFalse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCondition(), getExpressionTrue(), getExpressionFalse());
    }

    @Override
    public String toString() {
        return "[" + condition + "]?{" +
                 expressionTrue +
                "}:{" + expressionFalse +
                "}";
    }
}
