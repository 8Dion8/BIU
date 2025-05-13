import java.util.Map;

public class Nor extends BinaryExpression {
    public Nor(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !(super.getLeft().evaluate(assignment) || super.getRight().evaluate(assignment));
    }

    public String toString() {
        return super.toString(" V ");
    }

    public Expression assign(String var, Expression expression) {
        return new Nor(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }
}
