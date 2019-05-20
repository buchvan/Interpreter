package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Expression {

    abstract public int getValueByParams(Map<String, Integer> idToValue) throws InterpreterException;


    protected static final String RE_CONSTANT_EXPRESSION_INSERTED = "-?" + Interpreter.RE_NUMBER;
    protected static final String RE_CONSTANT_EXPRESSION_COMPLETELY = "^" + RE_CONSTANT_EXPRESSION_INSERTED + "$";

    // Identifier: 1;
    // First param: 2;
    // List of params: 3;
    // Count of groups: 3;
    protected static final String RE_CALL_EXPRESSION_INSERTED = "(" + Interpreter.RE_IDENTIFIER_INSERTED +
            ")\\(([^,]+)(,.+)*\\)";
    protected static final String RE_CALL_EXPRESSION_COMPLETELY = "^" + RE_CALL_EXPRESSION_INSERTED + "$";

    // Condition: 1;
    // Expression true: 10;
    // Expression false: 19;
    // Count of groups: 27;
    protected static final String RE_IF_EXPRESSION_INSERTED = "\\[((" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")|(\\[.+\\]\\?\\{.+\\}:\\{.+\\})|(\\(.+\\))|(" +
            RE_CONSTANT_EXPRESSION_INSERTED + ")|(" + RE_CALL_EXPRESSION_INSERTED + "))\\]" +
            "\\?\\{((" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")|(\\[.+\\]\\?\\{.+\\}:\\{.+\\})|(\\(.+\\))|(" +
            RE_CONSTANT_EXPRESSION_INSERTED + ")|(" + RE_CALL_EXPRESSION_INSERTED +
            "))\\}:\\{((" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")|(\\[.+\\]\\?\\{.+\\}:\\{.+\\})|(\\(.+\\))|(" +
            RE_CONSTANT_EXPRESSION_INSERTED + ")|(" + RE_CALL_EXPRESSION_INSERTED +
            "))\\}";
    protected static final String RE_IF_EXPRESSION_COMPLETELY = "^" + RE_IF_EXPRESSION_INSERTED + "$";

    // First expression: 1;
    // Operation: 37;
    // Second expression 38;
    // Count of groups: 73
    protected static final String RE_BINARY_EXPRESSION_INSERTED = "\\(" +
            "((" + Interpreter.RE_IDENTIFIER_INSERTED + ")|(" + RE_IF_EXPRESSION_INSERTED + ")|(" +
            RE_CONSTANT_EXPRESSION_INSERTED + ")|(\\(.+\\))|(" +
            RE_CALL_EXPRESSION_INSERTED + "))" +
            "(" + Interpreter.RE_OPERATION + ")" +
            "((" + Interpreter.RE_IDENTIFIER_INSERTED + ")|(" + RE_IF_EXPRESSION_INSERTED + ")|(" +
            RE_CONSTANT_EXPRESSION_INSERTED + ")|(\\(.+\\))|(" +
            RE_CALL_EXPRESSION_INSERTED + "))" +
            "\\)";
    protected static final String RE_BINARY_EXPRESSION_COMPLETELY = "^" + RE_BINARY_EXPRESSION_INSERTED + "$";

    protected static final Pattern CONSTANT_EXPRESSION_PATTERN = Pattern.compile(RE_CONSTANT_EXPRESSION_COMPLETELY);
    protected static final Pattern BINARY_EXPRESSION_PATTERN = Pattern.compile(RE_BINARY_EXPRESSION_COMPLETELY);
    protected static final Pattern IF_EXPRESSION_PATTERN = Pattern.compile(RE_IF_EXPRESSION_COMPLETELY);
    protected static final Pattern CALL_EXPRESSION_PATTERN = Pattern.compile(RE_CALL_EXPRESSION_COMPLETELY);



    public static Expression getExpression(String expression) throws InterpreterException {
        Matcher matcher;
        matcher = Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(expression);
        if (matcher.find()) {
            return new Variable(
                    expression.substring(matcher.start(), matcher.end()));
        }
        matcher = CONSTANT_EXPRESSION_PATTERN.matcher(expression);
        if (matcher.find()) {
            return new ConstantExpression(
                    expression.substring(matcher.start(), matcher.end())
            );
        }
        matcher = BINARY_EXPRESSION_PATTERN.matcher(expression);
        if (matcher.find()) {
            return new BinaryExpression(
                    Expression.getExpression(matcher.group(1)),
                    matcher.group(37),
                    Expression.getExpression(matcher.group(38))
            );
        }
        matcher = IF_EXPRESSION_PATTERN.matcher(expression);
        if (matcher.find()) {
            return new IfExpression(
                    Expression.getExpression(matcher.group(1)),
                    Expression.getExpression(matcher.group(10)),
                    Expression.getExpression(matcher.group(19))
            );
        }
        matcher = CALL_EXPRESSION_PATTERN.matcher(expression);
        if (matcher.find()) {
            return parseCallExpression(matcher);
        }
        throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
    }

    private static CallExpression parseCallExpression(Matcher matcher) throws InterpreterException {
        String[] arguments;
        if (matcher.group(3) != null) {
            arguments = matcher.group(3).substring(1).split(",");
        } else {
            arguments = new String[0];
        }
        List<Expression> argumentList = new ArrayList<>(1 + arguments.length);
        argumentList.add(Expression.getExpression(matcher.group(2)));
        for (String argument : arguments) {
            argumentList.add(Expression.getExpression(argument));
        }
        return new CallExpression(matcher.group(1), argumentList);
    }
}