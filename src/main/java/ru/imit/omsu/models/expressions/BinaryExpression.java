package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.models.expressions.enums.Operation;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

public class BinaryExpression extends Expression {

    private Expression firstExpression;
    private Operation operation;
    private Expression secondExpression;

    private static void checkOfStringOperation(String operation) throws InterpreterException {
        if (!Interpreter.OPERATION_PATTERN.matcher(operation).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
    }

    public BinaryExpression(Expression firstExpression, String operation, Expression secondExpression)
            throws InterpreterException {
        checkOfStringOperation(operation);
        this.firstExpression = firstExpression;
        this.operation = Operation.value(operation);
        this.secondExpression = secondExpression;
    }

    private static void checkOfOperation(Operation operation) throws InterpreterException {
        if (operation == null) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
    }

    public BinaryExpression(Expression firstExpression, Operation operation, Expression secondExpression)
            throws InterpreterException {
        checkOfOperation(operation);
        this.firstExpression = firstExpression;
        this.operation = operation;
        this.secondExpression = secondExpression;
    }

    private static Matcher checkOfStringBinaryExpression(String binaryExpression) throws InterpreterException {
        Matcher matcher = BINARY_EXPRESSION_PATTERN.matcher(binaryExpression);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        return matcher;
    }

    public BinaryExpression(String binaryExpression) throws InterpreterException {
        Matcher matcher = checkOfStringBinaryExpression(binaryExpression);
        firstExpression = Expression.getExpression(matcher.group(1));
        operation = Operation.value(matcher.group(6));
        secondExpression = Expression.getExpression(matcher.group(7));
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

    private int checkNotZero(Expression expression, Map<String, Integer> identifierToValue) throws InterpreterException {
        int temp = expression.getValueByParams(identifierToValue);
        if (temp == 0) {
            throw new InterpreterException(InterpreterErrorCode.RUNTIME_ERROR, this);
        }
        return temp;
    }

    @Override
    public int getValueByParams(Map<String, Integer> identifierToValue) throws InterpreterException {
        if (operation == Operation.PLUS) {
            return firstExpression.getValueByParams(identifierToValue)
                    + secondExpression.getValueByParams(identifierToValue);
        }
        if (operation == Operation.MINUS) {
            return firstExpression.getValueByParams(identifierToValue)
                    - secondExpression.getValueByParams(identifierToValue);
        }
        if (operation == Operation.MULTIPLY) {
            return firstExpression.getValueByParams(identifierToValue)
                    * secondExpression.getValueByParams(identifierToValue);
        }
        if (operation == Operation.DIVIDE) {
            return firstExpression.getValueByParams(identifierToValue)
                    / checkNotZero(secondExpression, identifierToValue);
        }
        if (operation == Operation.REMAINDER) {
            return firstExpression.getValueByParams(identifierToValue)
                    % checkNotZero(secondExpression, identifierToValue);
        }
        if (operation == Operation.MORE) {
            return firstExpression.getValueByParams(identifierToValue)
                    > secondExpression.getValueByParams(identifierToValue) ? 1 : 0;
        }
        if (operation == Operation.LESS) {
            return firstExpression.getValueByParams(identifierToValue)
                    < secondExpression.getValueByParams(identifierToValue) ? 1 : 0;
        }
        if (operation == Operation.EQUAL) {
            return firstExpression.getValueByParams(identifierToValue)
                    == secondExpression.getValueByParams(identifierToValue) ? 1 : 0;
        }
        throw new InterpreterException(InterpreterErrorCode.IMPOSSIBLE_ERROR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryExpression)) return false;
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(getFirstExpression(), that.getFirstExpression()) &&
                getOperation() == that.getOperation() &&
                Objects.equals(getSecondExpression(), that.getSecondExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstExpression(), getOperation(), getSecondExpression());
    }

    @Override
    public String toString() {
        return  "(" + firstExpression + ""  + operation.getSymbol() +
                "" + secondExpression + ")";
    }
}
