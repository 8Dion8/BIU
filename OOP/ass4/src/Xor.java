import java.util.Map;

/**
* Represents a logical XOR operation between two expressions.
* @author Gleb Shvartser 346832892
*/
public class Xor extends BinaryExpression {
    /**
    * Constructs a new Xor expression with the specified left and right sub-expressions.
    *
    * @param leftExpr  the left sub-expression of the XOR operation
    * @param rightExpr the right sub-expression of the XOR operation
    */
    public Xor(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return super.getLeft().evaluate(assignment) ^ super.getRight().evaluate(assignment);
    }

    @Override
    public String toString() {
        return super.toString(" ^ ");
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Xor(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }

    @Override
    public Expression nandify() {
        Expression a = super.getLeft().nandify();
        Expression b = super.getRight().nandify();
        return new Nand(
            new Nand(
                a,
                new Nand(a, b)
            ),
            new Nand(
                b,
                new Nand(a, b)
            )
        );
    }

    @Override
    public Expression norify() {
        Expression a = super.getLeft().norify();
        Expression b = super.getRight().norify();
        return new Nor(
            new Nor(
                new Nor(a, a),
                new Nor(b, b)
            ),
            new Nor(a, b)
        );
    }

    @Override
    public Expression simplify() {
        Expression simpleLeft = super.getLeft().simplify();
        Expression simpleRight = super.getRight().simplify();

        // try evaluating without variables
        // this will only not error if the whole expression consists of constants (T/F)
        try {
            return new Val(new Xor(simpleLeft, simpleRight).evaluate());
        } catch (Exception e) { }

        Expression falseV = new Val(false);
        Expression trueV = new Val(true);

        if (simpleLeft.equals(trueV)) {
            return new Not(simpleRight);
        }
        if (simpleRight.equals(trueV)) {
            return new Not(simpleLeft);
        }
        if (simpleLeft.equals(falseV)) {
            return simpleRight;
        }
        if (simpleRight.equals(falseV)) {
            return simpleLeft;
        }
        if (simpleLeft.equals(simpleRight)) {
            return falseV;
        }

        return new Xor(simpleLeft, simpleRight);
    }
}
