import java.util.List;

/**
 * Abstract class representing a binary expression consisting of two sub-expressions.
 *
 * @author Gleb Shvartser 346832892
 */
public abstract class BinaryExpression extends BaseExpression {
    private Expression leftExpr;
    private Expression rightExpr;

    /**
     * Constructs a BinaryExpression with the given left and right sub-expressions.
     *
     * @param leftExpr  the left sub-expression
     * @param rightExpr the right sub-expression
     */
    public BinaryExpression(Expression leftExpr, Expression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    /**
     * Retrieves a list of variables used in both the left and right sub-expressions.
     *
     * @return a merged list of variables from the left and right sub-expressions
     */
    public List<String> getVariables() {
        List<String> variables1 = this.leftExpr.getVariables();
        List<String> variables2 = this.rightExpr.getVariables();
        return Common.merge(variables1, variables2);
    }

    /**
     * Gets the left sub-expression of this binary expression.
     *
     * @return the left sub-expression
     */
    public Expression getLeft() {
        return this.leftExpr;
    }

    /**
     * Gets the right sub-expression of this binary expression.
     *
     * @return the right sub-expression
     */
    public Expression getRight() {
        return this.rightExpr;
    }

    /**
     * Returns a string representation of this binary expression using the specified operator.
     *
     * @param representation the operator to use between the left and right sub-expressions
     * @return a string representation of the binary expression
     */
    public String toString(String representation) {
        return "(" + this.leftExpr.toString() + representation + this.rightExpr.toString() + ")";
    }
}
