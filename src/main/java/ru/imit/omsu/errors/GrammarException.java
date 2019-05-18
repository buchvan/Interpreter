package ru.imit.omsu.errors;

import ru.imit.omsu.models.expressions.Expression;

public class GrammarException extends Exception {

    private ErrorCode errorCode;
    private String name;
    private Expression expression;
    private Integer line;

    public GrammarException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public GrammarException(ErrorCode errorCode, String name) {
        this.errorCode = errorCode;
        this.name = name;
    }

    public GrammarException(ErrorCode errorCode, String name, int line) {
        this.errorCode = errorCode;
        this.name = name;
        this.line = line;
    }

    public GrammarException(ErrorCode errorCode, Expression expression) {
        this.errorCode = errorCode;
        this.expression = expression;
    }

    public GrammarException(ErrorCode errorCode, Expression expression, int line) {
        this.errorCode = errorCode;
        this.expression = expression;
        this.line = line;
    }

    public ErrorCode getErrorCode() {
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
        if (errorCode == ErrorCode.RUNTIME_ERROR) {
            return errorCode.getMessage() + " " + expression + ":" + line;
        }
        if (errorCode == ErrorCode.PARAMETER_NOT_FOUND
                || errorCode == ErrorCode.FUNCTION_NOT_FOUND
                || errorCode == ErrorCode.ARGUMENT_NUMBER_MISMATCH) {
            return errorCode.getMessage() + " " + name + ":" + line;
        }
        return errorCode.getMessage();
    }
}
