package ru.imit.omsu.models.expressions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Expression {

    abstract public int getValueWithParams(Map<String, Integer> idToValue) throws InterpreterException;


    protected static final String RE_CONSTANT_EXPRESSION = "^-?" + Interpreter.RE_NUMBER + "$";
    protected static final String RE_BINARY_EXPRESSION = "^\\(" +
            "(([^()+\\-*/%><=]+)|(\\(.+\\))|(" +
            Interpreter.RE_IDENTIFIER_INSERTED +"\\(.+(,.+)*\\)))" +
            "(" + Interpreter.RE_OPERATION + ")" +
            "(([^()+\\-*/%><=]+)|(\\(.+\\))|(" +
            Interpreter.RE_IDENTIFIER_INSERTED + "\\(.+(,.+)*\\)))" +
            "\\)$";
    protected static final String RE_IF_EXPRESSION = "^\\[(.+)\\]" +
            "\\?\\{(.+)\\}" +
            ":\\{(.+)\\}$";
    protected static final String RE_CALL_EXPRESSION = "^([A-Za-z_]+)" +
            "\\(([^,]+)(,.+)*\\)$";

    protected static final Pattern CONSTANT_EXPRESSION_PATTERN = Pattern.compile(RE_CONSTANT_EXPRESSION);
    protected static final Pattern BINARY_EXPRESSION_PATTERN = Pattern.compile(RE_BINARY_EXPRESSION);
    protected static final Pattern IF_EXPRESSION_PATTERN = Pattern.compile(RE_IF_EXPRESSION);
    protected static final Pattern CALL_EXPRESSION_PATTERN = Pattern.compile(RE_CALL_EXPRESSION);



    public static Expression getExpression(String expression, Integer line) throws InterpreterException {
        Matcher matcher;
        matcher = Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(expression);
        if (matcher.find()) {
            return new Variable(
                    expression.substring(matcher.start(), matcher.end()), line);
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
                    Expression.getExpression(matcher.group(1), line),
                    matcher.group(6),
                    Expression.getExpression(matcher.group(7), line),
                    line
            );
        }
        matcher = IF_EXPRESSION_PATTERN.matcher(expression);
        if (matcher.find()) {
            return new IfExpression(
                    Expression.getExpression(matcher.group(1), line),
                    Expression.getExpression(matcher.group(2), line),
                    Expression.getExpression(matcher.group(3), line)
            );
        }
        matcher = CALL_EXPRESSION_PATTERN.matcher(expression);
        if (matcher.find()) {
            String[] arguments;
            if (matcher.group(3) != null) {
                arguments = matcher.group(3).substring(1).split(",");
            } else {
                arguments = new String[0];
            }
            List<Expression> argumentList = new ArrayList<>(1 + arguments.length);
            argumentList.add(Expression.getExpression(matcher.group(2), line));
            for (String argument : arguments) {
                argumentList.add(Expression.getExpression(argument, line));
            }
            return new CallExpression(matcher.group(1), argumentList, line);
        }
        throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
    }
}