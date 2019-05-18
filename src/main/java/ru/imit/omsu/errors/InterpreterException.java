package ru.imit.omsu.errors;

import ru.imit.omsu.models.expressions.Expression;

public class InterpreterException extends Exception {

    private InterpreterErrorCode errorCode;
    private String name;
    private Expression expression;
    private Integer line;

    public InterpreterException(InterpreterErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public InterpreterException(InterpreterErrorCode errorCode, String name) {
        this.errorCode = errorCode;
        this.name = name;
    }

    public InterpreterException(InterpreterErrorCode errorCode, String name, int line) {
        this.errorCode = errorCode;
        this.name = name;
        this.line = line;
    }

    public InterpreterException(InterpreterErrorCode errorCode, Expression expression) {
        this.errorCode = errorCode;
        this.expression = expression;
    }

    public InterpreterException(InterpreterErrorCode errorCode, Expression expression, int line) {
        this.errorCode = errorCode;
        this.expression = expression;
        this.line = line;
    }

    public InterpreterErrorCode getErrorCode() {
        return errorCode;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    @Override
    public String toString() {
        if (errorCode == InterpreterErrorCode.RUNTIME_ERROR) {
            return errorCode.getMessage() + " " + expression + ":" + line;
        }
        if (errorCode == InterpreterErrorCode.PARAMETER_NOT_FOUND
                || errorCode == InterpreterErrorCode.FUNCTION_NOT_FOUND
                || errorCode == InterpreterErrorCode.ARGUMENT_NUMBER_MISMATCH) {
            return errorCode.getMessage() + " " + name + ":" + line;
        }
        return errorCode.getMessage();
    }
}
