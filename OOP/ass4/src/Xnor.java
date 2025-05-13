import java.util.Map;

public class Xnor extends BinaryExpression {

    public Expression baseExpression1;
    public Expression baseExpression2;

    public Xnor(Expression expression1, Expression expression2) {
        this.baseExpression1 = expression1;
        this.baseExpression2 = expression2;
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !(this.baseExpression1.evaluate(assignment) ^ this.baseExpression2.evaluate(assignment));
    }

    public String toString() {
        return super.toString(" # ");
    }

    public Expression assign(String var, Expression expression) {
        return new Xnor(this.baseExpression1.assign(var, expression), this.baseExpression2.assign(var, expression));
    }
}
