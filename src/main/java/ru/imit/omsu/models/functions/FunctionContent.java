package ru.imit.omsu.models.functions;

import ru.imit.omsu.models.expressions.Expression;

import java.util.List;
import java.util.Objects;

public class FunctionContent {

    private List<String> params;
    private Expression expression;

    public FunctionContent(List<String> params, Expression expression) {
        this.params = params;
        this.expression = expression;
    }

    public List<String> getParams() {
        return params;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionContent)) return false;
        FunctionContent content = (FunctionContent) o;
        return Objects.equals(getParams(), content.getParams()) &&
                Objects.equals(getExpression(), content.getExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParams(), getExpression());
    }

    @Override
    public String toString() {
        return "(" + params +
                ")={" + expression +
                '}';
    }
}
