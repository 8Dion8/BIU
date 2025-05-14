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

    public Expression nandify() {
        return this;
    }

    public Expression norify() {
        Expression a = super.getLeft().norify();
        Expression b = super.getRight().norify();
        return new Nor(
            new Nor(
                new Nor(a, a),
                new Nor(b, b)
            ),
            new Nor(
                new Nor(a, a),
                new Nor(b, b)
            )
        );
    }
}
