import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Not extends UnaryExpression {
    public Not(Expression expr) {
        super(expr);
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !super.getExpr().evaluate(assignment);
    }

    public String toString() {
        return super.toString("~");
    }

    public Expression assign(String var, Expression expression) {
        return new Not(super.getExpr().assign(var, expression));
    }
}
