package ru.imit.omsu.models.expressions.enums;

import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

public enum Operation {

    PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/'), REMAINDER('%'), MORE('>'), LESS('<'), EQUAL('=');

    private char symbol;

    Operation(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static Operation value(String string) throws InterpreterException {
        if (string.length() == 1) {
            for (Operation operation : Operation.values()) {
                if (operation.getSymbol() == string.charAt(0)) {
                    return operation;
                }
            }
        }
        throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
    }
}
