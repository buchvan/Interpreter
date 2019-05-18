package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;
import ru.imit.omsu.models.functions.FunctionDefinition;

import java.util.*;
import java.util.regex.Matcher;

public class CallExpression extends Expression {

    private String identifier;
    private List<Expression> argumentList;
    private Integer line;

    public CallExpression(String identifier, List<Expression> argumentList, Integer line) throws GrammarException {
        if (!Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(identifier).find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        this.identifier = identifier;
        this.argumentList = argumentList;
        this.line = line;
    }

    public CallExpression(String callExpression, Integer line) throws GrammarException {
        Matcher matcher = CALL_EXPRESSION_PATTERN.matcher(callExpression);
        if (!matcher.find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        this.line = line;
        String[] arguments;
        if (matcher.group(3) != null) {
            arguments = matcher.group(3).split(",");
        } else {
            arguments = new String[0];
        }
        List<Expression> argumentList = new ArrayList<>(1 + arguments.length);
        argumentList.add(Expression.getExpression(matcher.group(2), line));
        for (String argument : arguments) {
            argumentList.add(Expression.getExpression(argument, line));
        }
        identifier = matcher.group(1);
        this.argumentList = argumentList;
    }

    @Override
    public int getValueWithParams(Map<String, Integer> idToValue) throws GrammarException {
        FunctionDefinition functionDefinition = Interpreter.program.getFunctionDefinition(identifier, line);
        if (functionDefinition.getParameterList().size() != argumentList.size()) {
            throw new GrammarException(ErrorCode.ARGUMENT_NUMBER_MISMATCH, identifier, line);
        }
        List<String> params = functionDefinition.getParameterList();
        Expression expression = functionDefinition.getExpression();
        Map<String, Integer> identifierToValue = new HashMap<>(argumentList.size());
        int i = 0;
        for (Expression expressionArgument : argumentList) {
            identifierToValue.put(params.get(i), expressionArgument.getValueWithParams(idToValue));
            i++;
        }
        System.out.println(this + " = " + expression);

        //DEBUG
        int res = expression.getValueWithParams(identifierToValue);
        System.out.println("---------------");
        System.out.println(this + " = " + expression);
        System.out.println(res);
        return res;
        //DEBUG

//            return expression.getValueWithParams(identifierToValue)
    }

    @Override
    public Integer getLine() {
        return line;
    }

    @Override
    public String toString() {
        return  identifier + '\'' +
                "(" + argumentList +
                ")";
    }
}
