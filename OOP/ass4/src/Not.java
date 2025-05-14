import java.util.Map;

/**
* The Not class represents a logical NOT operation in a unary expression.
* @author Gleb Shvartser 346832892
*/
public class Not extends UnaryExpression {

    /**
    * Constructs a Not expression with the specified operand.
    *
    * @param expr the expression to be negated
    */
    public Not(Expression expr) {
        super(expr);
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !super.getExpr().evaluate(assignment);
    }

    @Override
    public String toString() {
        return super.toString("~");
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return new Not(super.getExpr().assign(var, expression));
    }

    @Override
    public Expression nandify() {
        return new Nand(super.getExpr().nandify(), super.getExpr().nandify());
    }

    @Override
    public Expression norify() {
        return new Nor(super.getExpr().norify(), super.getExpr().norify());
    }

    @Override
    public Expression simplify() {
        Expression simpleExpr = super.getExpr().simplify();

        // try evaluating without variables
        // this will only not error if the whole expression consists of constants (T/F)
        try {
            return new Val(new Not(simpleExpr).evaluate());
        } catch (Exception e) { }

        return new Not(simpleExpr);
    }
}
