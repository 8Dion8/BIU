import java.util.Map;

public class Nand extends BinaryExpression {
    public Nand(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);

    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !(super.getLeft().evaluate(assignment) && super.getRight().evaluate(assignment));
    }

    public String toString() {
        return super.toString(" A ");
    }

    public Expression assign(String var, Expression expression) {
        return new Nand(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }
}
