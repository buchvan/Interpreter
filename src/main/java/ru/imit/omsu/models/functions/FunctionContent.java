package ru.imit.omsu.models.functions;

import ru.imit.omsu.models.expressions.Expression;

import java.util.List;
import java.util.Objects;

public class FunctionContent {

    private List<String> parameterList;
    private Expression expression;

    public FunctionContent(List<String> parameterList, Expression expression) {
        this.parameterList = parameterList;
        this.expression = expression;
    }

    public List<String> getParameterList() {
        return parameterList;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionContent)) return false;
        FunctionContent content = (FunctionContent) o;
        return Objects.equals(getParameterList(), content.getParameterList()) &&
                Objects.equals(getExpression(), content.getExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParameterList(), getExpression());
    }

    @Override
    public String toString() {
        return "(" + parameterList +
                ")={" + expression +
                '}';
    }
}
