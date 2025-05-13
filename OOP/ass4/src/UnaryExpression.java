import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

public abstract class UnaryExpression implements Expression {
    private Expression expr;

    public UnaryExpression(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return this.expr;
    }

    public Boolean evaluate() throws Exception {
        return this.evaluate(new TreeMap<String, Boolean>());
    }

    public List<String> getVariables() {
        return this.expr.getVariables();
    }

    public String toString(String representation) {
        return representation + "(" + this.expr.toString() + ")";
    }
}
