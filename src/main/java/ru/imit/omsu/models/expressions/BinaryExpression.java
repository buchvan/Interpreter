package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.models.expressions.enums.Operation;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.Map;
import java.util.regex.Matcher;

public class BinaryExpression extends Expression {

    private Expression firstExpression;
    private Operation operation;
    private Expression secondExpression;
    private Integer line;

    public BinaryExpression(Expression firstExpression, String operation, Expression secondExpression, Integer line)
            throws InterpreterException {
        if (!Interpreter.OPERATION_PATTERN.matcher(operation).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        this.firstExpression = firstExpression;
        this.operation = Operation.value(operation);
        this.secondExpression = secondExpression;
        this.line = line;
    }

    public BinaryExpression(Expression firstExpression, Operation operation, Expression secondExpression, Integer line)
            throws InterpreterException {
        if (operation == null) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        this.firstExpression = firstExpression;
        this.operation = operation;
        this.secondExpression = secondExpression;
        this.line = line;
    }

    public BinaryExpression(String binaryExpression, Integer line) throws InterpreterException {
        Matcher matcher = BINARY_EXPRESSION_PATTERN.matcher(binaryExpression);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
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
    public int getValueWithParams(Map<String, Integer> idToValue) throws InterpreterException {
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
                throw new InterpreterException(InterpreterErrorCode.RUNTIME_ERROR, this, line);
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
        throw new InterpreterException(InterpreterErrorCode.IMPOSSIBLE_ERROR);
    }

    @Override
    public String toString() {
        return  "(" + firstExpression + ""  + operation.getSymbol() +
                "" + secondExpression + ")";
    }
}
