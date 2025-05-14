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

    public Expression simplify() {
        Expression simpleLeft = super.getLeft().simplify();
        Expression simpleRight = super.getRight().simplify();

        // try evaluating without variables
        // this will only not error if the whole expression consists of constants (T/F)
        try {
            return new Val(new Nand(simpleLeft, simpleRight).evaluate());
        } catch (Exception e) {}

        Expression falseV = new Val(false);
        Expression trueV = new Val(true);

        if (simpleLeft.equals(trueV)) {
            return new Not(simpleRight);
        }
        if (simpleRight.equals(trueV)) {
            return new Not(simpleLeft);
        }
        if (simpleLeft.equals(falseV) || simpleRight.equals(falseV)) {
            return trueV;
        }
        if (simpleLeft.equals(simpleRight)) {
            return new Not(simpleLeft);
        }

        return new Nand(simpleLeft, simpleRight);
    }
}
