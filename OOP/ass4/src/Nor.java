import java.util.Map;

/**
* Represents a logical NOR operation between two expressions.
* @author Gleb Shvartser 346832892
*/
public class Nor extends BinaryExpression {
    /**
    * Constructs a new Nor expression with the specified left and right sub-expressions.
    *
    * @param leftExpr  the left sub-expression of the XOR operation
    * @param rightExpr the right sub-expression of the XOR operation
    */
    public Nor(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !(super.getLeft().evaluate(assignment) || super.getRight().evaluate(assignment));
    }

    @Override
    public String toString() {
        return super.toString(" V ");
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Nor(super.getLeft().assign(var, expression), super.getRight().assign(var, expression));
    }

    @Override
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

    @Override
    public Expression norify() {
        return this;
    }

    @Override
    public Expression simplify() {
        Expression simpleLeft = super.getLeft().simplify();
        Expression simpleRight = super.getRight().simplify();

        // try evaluating without variables
        // this will only not error if the whole expression consists of constants (T/F)
        try {
            return new Val(new Nor(simpleLeft, simpleRight).evaluate());
        } catch (Exception e) { }

        Expression falseV = new Val(false);
        Expression trueV = new Val(true);

        if (simpleLeft.equals(trueV) || simpleRight.equals(trueV)) {
            return falseV;
        }
        if (simpleLeft.equals(falseV)) {
            return new Not(simpleRight);
        }
        if (simpleRight.equals(falseV)) {
            return new Not(simpleLeft);
        }
        if (simpleLeft.equals(simpleRight)) {
            return new Not(simpleLeft);
        }

        return new Nand(simpleLeft, simpleRight);
    }
}
