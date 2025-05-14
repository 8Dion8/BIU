import java.util.List;

abstract public class BinaryExpression extends BaseExpression {
    public Expression leftExpr;
    public Expression rightExpr;

    public BinaryExpression(Expression leftExpr, Expression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public List<String> getVariables() {
        List<String> variables1 = this.leftExpr.getVariables();
        List<String> variables2 = this.rightExpr.getVariables();
        for (String var: variables2) {
            if (!variables1.contains(var)) {
                variables1.add(var);
            }
        }
        return variables1;
    }

    public Expression getLeft() {
        return this.leftExpr;
    }

    public Expression getRight() {
        return this.rightExpr;
    }

    public String toString(String representation) {
        return "(" + this.leftExpr.toString() + representation + this.rightExpr.toString() + ")";
    }
}
