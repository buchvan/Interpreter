package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.models.expressions.enums.Operation;
import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;

import java.util.Map;
import java.util.regex.Matcher;

public class BinaryExpression extends Expression {

    private Expression firstExpression;
    private Operation operation;
    private Expression secondExpression;
    private Integer line;

    public BinaryExpression(Expression firstExpression, String operation, Expression secondExpression, Integer line)
            throws GrammarException {
        if (!Interpreter.OPERATION_PATTERN.matcher(operation).find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        this.firstExpression = firstExpression;
        this.operation = Operation.value(operation);
        this.secondExpression = secondExpression;
        this.line = line;
    }

    public BinaryExpression(Expression firstExpression, Operation operation, Expression secondExpression, Integer line)
            throws GrammarException {
        if (operation == null) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        this.firstExpression = firstExpression;
        this.operation = operation;
        this.secondExpression = secondExpression;
        this.line = line;
    }

    public BinaryExpression(String binaryExpression, Integer line) throws GrammarException {
        Matcher matcher = BINARY_EXPRESSION_PATTERN.matcher(binaryExpression);
        if (!matcher.find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        firstExpression = Expression.getExpression(matcher.group(1), line);
        operation = Operation.value(matcher.group(6));
        secondExpression = Expression.getExpression(matcher.group(7), line);
        this.line = line;
    }

    public Expression getFirstExpression() {
        return firstExpression;
    }

    public Operation getOperation() {
        return operation;
    }

    public Expression getSecondExpression() {
        return secondExpression;
    }

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws GrammarException {
        if (operation == Operation.PLUS) {
            return firstExpression.getValueWithParams(idToValue) + secondExpression.getValueWithParams(idToValue);
        }
        if (operation == Operation.MINUS) {
            return firstExpression.getValueWithParams(idToValue) - secondExpression.getValueWithParams(idToValue);
        }
        if (operation == Operation.MULTIPLY) {
            return firstExpression.getValueWithParams(idToValue) * secondExpression.getValueWithParams(idToValue);
        }
        if (operation == Operation.DIVIDE) {
            int temp = secondExpression.getValueWithParams(idToValue);
            if (temp == 0) {
                throw new GrammarException(ErrorCode.RUNTIME_ERROR, this, line);
            }
            return firstExpression.getValueWithParams(idToValue) / temp;
        }
        if (operation == Operation.REMAINDER) {
            return firstExpression.getValueWithParams(idToValue) % secondExpression.getValueWithParams(idToValue);
        }
        if (operation == Operation.MORE) {
            return firstExpression.getValueWithParams(idToValue) > secondExpression.getValueWithParams(idToValue) ? 1 : 0;
        }
        if (operation == Operation.LESS) {
            return firstExpression.getValueWithParams(idToValue) < secondExpression.getValueWithParams(idToValue) ? 1 : 0;
        }
        if (operation == Operation.EQUAL) {
            return firstExpression.getValueWithParams(idToValue) == secondExpression.getValueWithParams(idToValue) ? 1 : 0;
        }
        throw new GrammarException(ErrorCode.IMPOSSIBLE_ERROR);
    }

    @Override
    public Integer getLine() {
        return line;
    }

    @Override
    public String toString() {
        return  "(" + firstExpression + ""  + operation.getSymbol() +
                "" + secondExpression + ")";
    }
}
