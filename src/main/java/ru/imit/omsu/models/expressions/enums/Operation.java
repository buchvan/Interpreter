package ru.imit.omsu.models.expressions.enums;

import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;

public enum Operation {

    PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/'), REMAINDER('%'), MORE('>'), LESS('<'), EQUAL('=');

    private char symbol;

    Operation(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static Operation value(String string) throws GrammarException {
        if (string.length() == 1) {
            for (Operation operation : Operation.values()) {
                if (operation.getSymbol() == string.charAt(0)) {
                    return operation;
                }
            }
        }
        throw new GrammarException(ErrorCode.SYNTAX_ERROR);
    }
}
