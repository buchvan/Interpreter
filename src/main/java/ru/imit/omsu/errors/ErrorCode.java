package ru.imit.omsu.errors;

public enum ErrorCode {

    SYNTAX_ERROR("SYNTAX ERROR"),
    PARAMETER_NOT_FOUND("PARAMETER NOT FOUND"),
    FUNCTION_NOT_FOUND("FUNCTION NOT FOUND"),
    ARGUMENT_NUMBER_MISMATCH("ARGUMENT NUMBER MISMATCH"),
    RUNTIME_ERROR("RUNTIME ERROR"),
    IMPOSSIBLE_ERROR("IMPOSSIBLE ERROR");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
