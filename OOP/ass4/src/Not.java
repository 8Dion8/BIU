import java.util.Map;

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

    public Expression nandify() {
        return new Nand(super.getExpr().nandify(), super.getExpr().nandify());
    }

    public Expression norify() {
        return new Nor(super.getExpr().norify(), super.getExpr().norify());
    }

    public Expression simplify() {
        Expression simpleExpr = super.getExpr().simplify();

        try {
            return new Val(new Not(simpleExpr).evaluate());
        } catch (Exception e) {}

        return new Not(simpleExpr);
    }
}
