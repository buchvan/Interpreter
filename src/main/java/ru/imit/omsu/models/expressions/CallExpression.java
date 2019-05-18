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
        if (!Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(identifier).find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        this.identifier = identifier;
        this.argumentList = argumentList;
    }

    public CallExpression(String callExpression) throws InterpreterException {
        Matcher matcher = CALL_EXPRESSION_PATTERN.matcher(callExpression);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
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

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws InterpreterException {
        FunctionDefinition functionDefinition;
        try {
            functionDefinition = Interpreter.program.getFunctionDefinition(identifier);
        } catch (InterpreterException ex) {
            ex.setLine(Interpreter.program.getLinesCount());
            throw ex;
        }
        if (functionDefinition.getParameterList().size() != argumentList.size()) {
            throw new InterpreterException(InterpreterErrorCode.ARGUMENT_NUMBER_MISMATCH, identifier,
                    Interpreter.program.getLinesCount());
        }
        List<String> params = functionDefinition.getParameterList();
        Expression expression = functionDefinition.getExpression();
        Map<String, Integer> identifierToValue = new HashMap<>(argumentList.size());
        int i = 0;
        try {
            for (Expression expressionArgument : argumentList) {
                identifierToValue.put(params.get(i), expressionArgument.getValueWithParams(idToValue));
                i++;
            }
        } catch (InterpreterException ex) {
            ex.setLine(Interpreter.program.getLinesCount());
            throw ex;
        }

        //DEBUG
//        System.out.println(this + " = " + expression);
//        int res = expression.getValueWithParams(identifierToValue);
//        System.out.println("---------------");
//        System.out.println(this + " = " + expression);
//        System.out.println(res);
//        return res;
        //DEBUG

        try {
            return expression.getValueWithParams(identifierToValue);
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
