package ru.imit.omsu.models.functions;

import ru.imit.omsu.Interpreter;
import ru.imit.omsu.errors.ErrorCode;
import ru.imit.omsu.errors.GrammarException;
import ru.imit.omsu.models.expressions.Expression;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionDefinition {

    private String identifier;
    private FunctionContent content;

    public FunctionDefinition(String identifier, List<String> parameterList, Expression expression)
            throws GrammarException {
        if (!Interpreter.IDENTIFIER_COMPLETELY_PATTERN.matcher(identifier).find()) {
            throw new GrammarException(ErrorCode.SYNTAX_ERROR);
        }
        this.identifier = identifier;
        content = new FunctionContent(parameterList, expression);
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getParameterList() {
        return content.getParams();
    }

    public Expression getExpression() {
        return content.getExpression();
    }



    protected static final String RE_FUNCTION_DEFINITION = "^(" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")\\((" +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")(," +
            Interpreter.RE_IDENTIFIER_INSERTED +
            ")*\\)=\\{(.+)\\}$";

    protected static final Pattern FUNCTION_DEFINITION_PATTERN = Pattern.compile(RE_FUNCTION_DEFINITION);

    public static FunctionDefinition getFunctionDefinition(String functionDefinition, int line) throws GrammarException {
        Matcher matcher = FUNCTION_DEFINITION_PATTERN.matcher(functionDefinition);
        if (matcher.find()) {
            String[] params;
            if (matcher.group(3) != null) {
                params = matcher.group(3).substring(1).split(",");
            } else {
                params = new String[0];
            }
            List<String> parameterList = new ArrayList<>(1 + params.length);
            parameterList.add(matcher.group(2));
            parameterList.addAll(Arrays.asList(params));
            return new FunctionDefinition(matcher.group(1), parameterList, Expression.getExpression(matcher.group(4), line));
        }
        throw new GrammarException(ErrorCode.SYNTAX_ERROR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionDefinition)) return false;
        FunctionDefinition that = (FunctionDefinition) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, content);
    }

    @Override
    public String toString() {
        return identifier + content.toString();
    }
}
