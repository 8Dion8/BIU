import java.util.Map;

public class Xnor extends BinaryExpression {
    public Xnor(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !(super.getLeft().evaluate(assignment) ^ super.getRight().evaluate(assignment));
    }

    public String toString() {
        return super.toString(" # ");
    }

    public Expression assign(String var, Expression expression) {
        return new Xnor(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }

    public Expression nandify() {
        Expression a = super.getLeft().nandify();
        Expression b = super.getRight().nandify();
        return new Nand(
            new Nand(
                new Nand(a, a),
                new Nand(b, b)
            ),
            new Nand(a, b)
        );
    }

    public Expression norify() {
        Expression a = super.getLeft().norify();
        Expression b = super.getRight().norify();
        return new Nor(
            new Nor(
                a,
                new Nor(a, b)
            ),
            new Nor(
                b,
                new Nor(a, b)
            )
        );
    }
}
