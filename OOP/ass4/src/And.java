import java.util.Map;

public class And extends BinaryExpression {
    public And(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return super.getLeft().evaluate(assignment) && super.getRight().evaluate(assignment);
    }

    public String toString() {
        return super.toString(" & ");
    }

    public Expression assign(String var, Expression expression) {
        return new And(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }

    public Expression nandify() {
        Expression a = super.getLeft().nandify();
        Expression b = super.getRight().nandify();
        return new Nand(
            new Nand(a, b),
            new Nand(a, b)
        );
    }

    public Expression norify() {
        Expression a = super.getLeft().norify();
        Expression b = super.getRight().norify();
        return new Nor(
            new Nor(a, a),
            new Nor(b, b)
        );
    }

    public Expression simplify() {
        Expression simpleLeft = super.getLeft().simplify();
        Expression simpleRight = super.getRight().simplify();

        try {
            return new Val(new And(simpleLeft, simpleRight).evaluate());
        } catch (Exception e) {}

        Expression falseV = new Val(false);
        Expression trueV = new Val(true);

        if (simpleLeft.equals(trueV)) {
            return simpleRight;
        }
        if (simpleRight.equals(trueV)) {
            return simpleLeft;
        }
        if (simpleLeft.equals(falseV) || simpleRight.equals(falseV)) {
            return falseV;
        }
        if (simpleLeft.equals(simpleRight)) {
            return simpleLeft;
        }

        return new And(simpleLeft, simpleRight);
    }
}
