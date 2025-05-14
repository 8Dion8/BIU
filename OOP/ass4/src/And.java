import java.util.Map;

/**
* Represents a logical AND operation between two expressions.
* @author Gleb Shvartser 346832892
*/
public class And extends BinaryExpression {
    /**
    * Constructs a new And expression with the specified left and right sub-expressions.
    *
    * @param leftExpr  the left sub-expression of the XOR operation
    * @param rightExpr the right sub-expression of the XOR operation
    */
    public And(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return super.getLeft().evaluate(assignment) && super.getRight().evaluate(assignment);
    }

    @Override
    public String toString() {
        return super.toString(" & ");
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new And(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }

    @Override
    public Expression nandify() {
        Expression a = super.getLeft().nandify();
        Expression b = super.getRight().nandify();
        return new Nand(
            new Nand(a, b),
            new Nand(a, b)
        );
    }

    @Override
    public Expression norify() {
        Expression a = super.getLeft().norify();
        Expression b = super.getRight().norify();
        return new Nor(
            new Nor(a, a),
            new Nor(b, b)
        );
    }

    @Override
    public Expression simplify() {
        Expression simpleLeft = super.getLeft().simplify();
        Expression simpleRight = super.getRight().simplify();

        // try evaluating without variables
        // this will only not error if the whole expression consists of constants (T/F)
        try {
            return new Val(new And(simpleLeft, simpleRight).evaluate());
        } catch (Exception e) { }

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
