package ru.imit.omsu.models.functions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.InterpreterErrorCode;
import ru.imit.omsu.errors.InterpreterException;
import ru.imit.omsu.models.expressions.Expression;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionDefinition {

    private String identifier;
    private List<String> parameterList;
    private Expression expression;
    private int lineNumber;

    public FunctionDefinition(String identifier, List<String> parameterList, Expression expression, int lineNumber)
            throws InterpreterException {
        Interpreter.checkOfIdentifier(identifier);
        this.identifier = identifier;
        this.parameterList = parameterList;
        this.expression = expression;
        this.lineNumber = lineNumber;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getParameterList() {
        return parameterList;
    }

    public Expression getExpression() {
        return expression;
    }

    public int getLine() {
        return lineNumber;
    }


    protected static final String RE_FUNCTION_DEFINITION = "^(" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")\\((" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")(," +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")*\\)=\\{(.+)\\}$";

    protected static final Pattern FUNCTION_DEFINITION_PATTERN = Pattern.compile(RE_FUNCTION_DEFINITION);


    private static Matcher checkOfStringFunctionDefinition(String functionDefinition) throws InterpreterException {
        Matcher matcher = FUNCTION_DEFINITION_PATTERN.matcher(functionDefinition);
        if (!matcher.find()) {
            throw new InterpreterException(InterpreterErrorCode.SYNTAX_ERROR);
        }
        return matcher;
    }

    public static FunctionDefinition getFunctionDefinition(String functionDefinition, int line) throws InterpreterException {
        Matcher matcher = checkOfStringFunctionDefinition(functionDefinition);
        String[] params;
        if (matcher.group(3) != null) {
            params = matcher.group(3).substring(1).split(",");
        } else {
            params = new String[0];
        }
        List<String> parameterList = new ArrayList<>(1 + params.length);
        parameterList.add(matcher.group(2));
        parameterList.addAll(Arrays.asList(params));
        return new FunctionDefinition(matcher.group(1), parameterList, Expression.getExpression(matcher.group(4)), line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionDefinition)) return false;
        FunctionDefinition that = (FunctionDefinition) o;
        return getLine() == that.getLine() &&
                Objects.equals(getIdentifier(), that.getIdentifier()) &&
                Objects.equals(getParameterList(), that.getParameterList()) &&
                Objects.equals(getExpression(), that.getExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getParameterList(), getExpression(), getLine());
    }

    @Override
    public String toString() {
        return identifier + "(" + parameterList + ")={" + expression + "}";
    }
}
