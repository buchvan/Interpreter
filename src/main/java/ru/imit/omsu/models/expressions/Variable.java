package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;

import java.util.Map;
import java.util.Objects;

public class Variable extends Expression {

    private String identifier;
    private Integer value;
    private Integer line;

    public Variable(String identifier, Integer value, Integer line) throws GrammarException {
        if (!Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(identifier).find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        this.identifier = identifier;
        this.value = value;
        this.line = line;
    }

    public Variable(String identifier, Integer line) throws GrammarException {
        this(identifier, null, line);
    }

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws GrammarException {
        Integer value = idToValue.get(identifier);
        if (value == null) {
            throw new GrammarException(ErrorCode.PARAMETER_NOT_FOUND, identifier, line);
        }
        this.value = value;
        return value;
    }

    @Override
    public Integer getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return value == null && variable.value == null
                || value != null && variable.value != null
                && value.equals(variable.value) &&
                Objects.equals(identifier, variable.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, value);
    }

    @Override
    public String toString() {
        //DEBUG
//        if (value == null) {
//            return identifier;
//        }
//        return identifier + "=" + value;
        //DEBUG

        return identifier;
    }
}
