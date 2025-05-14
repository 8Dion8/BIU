import java.util.List;

/**
* The UnaryExpression class represents an abstract base class for expressions that operate on a single inner expression.
* @author Gleb Shvartser 346832892
*/
public abstract class UnaryExpression extends BaseExpression {
    private Expression expr;

    /**
     * Constructs a UnaryExpression with the given expression.
     *
     * @param expr the expression to be wrapped by this UnaryExpression
     */
    public UnaryExpression(Expression expr) {
        this.expr = expr;
    }

    /**
     * Returns the inner expression of this UnaryExpression.
     *
     * @return the inner expression
     */
    public Expression getExpr() {
        return this.expr;
    }

    /**
     * Retrieves the list of variables used in the inner expression.
     *
     * @return a list of variable names
     */
    public List<String> getVariables() {
        return this.expr.getVariables();
    }

    /**
     * Returns a string representation of this UnaryExpression using the provided representation.
     *
     * @param representation the string representation of the unary operator
     * @return a string representation of this UnaryExpression
     */
    public String toString(String representation) {
        return representation + "(" + this.expr.toString() + ")";
    }
}
