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

    public Expression nandify() {
        Expression a = super.getLeft().nandify();
        Expression b = super.getRight().nandify();
        return new Nand(
            new Nand(
                new Nand(a, a),
                new Nand(b, b)
            ),
            new Nand(
                new Nand(a, a),
                new Nand(b, b)
            )
        );
    }

    public Expression norify() {
        return this;
    }
}
