package ru.imit.omsu.models;

import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;
import ru.imit.omsu.models.expressions.Expression;
import ru.imit.omsu.models.functions.FunctionDefinition;

import java.util.HashMap;
import java.util.Map;

public class Program {

    private Map<String, FunctionDefinition> functionDefinitions;
    private Expression expression;

    public Program() {
        this.functionDefinitions = new HashMap<>();
    }

    public void addFunctionDefinition(FunctionDefinition functionDefinition) throws InterpreterException {
        String identifier = functionDefinition.getIdentifier();
        if (functionDefinitions.containsKey(identifier)) {
            throw new InterpreterException(InterpreterErrorCode.RUNTIME_ERROR);
        }
        functionDefinitions.put(identifier, functionDefinition);
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Map<String, FunctionDefinition> getFunctionDefinitionList() {
        return functionDefinitions;
    }

    public FunctionDefinition getFunctionDefinition(String identifier, int line) throws InterpreterException {
        FunctionDefinition functionDefinition = functionDefinitions.get(identifier);
        if (functionDefinition == null) {
            throw new InterpreterException(InterpreterErrorCode.FUNCTION_NOT_FOUND, identifier, line);
        }
        return functionDefinition;
    }

    public int run() throws InterpreterException {
        return expression.getValueWithParams(new HashMap<>(0));
    }
}
