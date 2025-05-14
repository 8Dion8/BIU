import java.util.List;

public abstract class UnaryExpression extends BaseExpression {
    private Expression expr;

    public UnaryExpression(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return this.expr;
    }

    public List<String> getVariables() {
        return this.expr.getVariables();
    }

    public String toString(String representation) {
        return representation + "(" + this.expr.toString() + ")";
    }
}
