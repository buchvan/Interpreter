package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.Map;
import java.util.Objects;

public class Variable extends Expression {

    private String identifier;

    public Variable(String identifier) throws InterpreterException {
        Interpreter.checkOfIdentifier(identifier);
        this.identifier = identifier;
    }

    @Override
    public int getValueByParams(Map<String, Integer> idToValue) throws InterpreterException {
        Integer value = idToValue.get(identifier);
        if (value == null) {
            throw new InterpreterException(InterpreterErrorCode.PARAMETER_NOT_FOUND, identifier);
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return Objects.equals(identifier, variable.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return identifier;
    }
}
