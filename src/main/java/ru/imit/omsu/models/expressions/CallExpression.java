package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;
import ru.imit.omsu.models.functions.FunctionDefinition;

import java.util.*;
import java.util.regex.Matcher;

public class CallExpression extends Expression {

    private String identifier;
    private List<Expression> argumentList;

    public CallExpression(String identifier, List<Expression> argumentList) throws InterpreterException {
        Interpreter.checkOfIdentifier(identifier);
        this.identifier = identifier;
        this.argumentList = argumentList;
    }

    private static Matcher checkOfStringCallExpression(String callExpression) throws InterpreterException {
        Matcher matcher = CALL_EXPRESSION_PATTERN.matcher(callExpression);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        return matcher;
    }

    public CallExpression(String callExpression) throws InterpreterException {
        Matcher matcher = checkOfStringCallExpression(callExpression);
        String[] arguments;
        if (matcher.group(3) != null) {
            arguments = matcher.group(3).split(",");
        } else {
            arguments = new String[0];
        }
        List<Expression> argumentList = new ArrayList<>(1 + arguments.length);
        argumentList.add(Expression.getExpression(matcher.group(2)));
        for (String argument : arguments) {
            argumentList.add(Expression.getExpression(argument));
        }
        identifier = matcher.group(1);
        this.argumentList = argumentList;
    }

    private void checkOfParametersCount(List<String> parameterList) throws InterpreterException {
        if (parameterList.size() != argumentList.size()) {
            throw new InterpreterException(InterpreterErrorCode.ARGUMENT_NUMBER_MISMATCH, identifier,
                    Interpreter.program.getLinesCount());
        }
    }

    @Override
    public int getValueWithParams(Map<String, Integer> identifierToValueFromUp) throws InterpreterException {
        FunctionDefinition functionDefinition;
        try {
            functionDefinition = Interpreter.program.getFunctionDefinition(identifier);
        } catch (InterpreterException ex) {
            if (ex.getErrorCode() == InterpreterErrorCode.FUNCTION_NOT_FOUND) {
                ex.setLine(Interpreter.program.getLinesCount());
            }
            throw ex;
        }
        checkOfParametersCount(functionDefinition.getParameterList());
        List<String> params = functionDefinition.getParameterList();
        Expression expression = functionDefinition.getExpression();
        Map<String, Integer> identifierToValueIts = new HashMap<>(argumentList.size());
        int currentParameterIndex = 0;
        try {
            for (Expression expressionArgument : argumentList) {
                identifierToValueIts.put(params.get(currentParameterIndex),
                        expressionArgument.getValueWithParams(identifierToValueFromUp));
                currentParameterIndex++;
            }
        } catch (InterpreterException ex) {
            ex.setLine(Interpreter.program.getLinesCount());
            throw ex;
        }
        try {
            return expression.getValueWithParams(identifierToValueIts);
        } catch (InterpreterException ex) {
            ex.setLine(functionDefinition.getLine());
            throw ex;
        }
    }

    @Override
    public String toString() {
        return  identifier + '\'' +
                "(" + argumentList +
                ")";
    }
}
