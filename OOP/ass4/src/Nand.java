import java.util.Map;

public class Nand extends BinaryExpression {

    public Expression baseExpression1;
    public Expression baseExpression2;

    public Nand(Expression expression1, Expression expression2) {
        this.baseExpression1 = expression1;
        this.baseExpression2 = expression2;
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !(this.baseExpression1.evaluate(assignment) && this.baseExpression2.evaluate(assignment));
    }

    public String toString() {
        return super.toString(" A ");
    }

    public Expression assign(String var, Expression expression) {
        return new Nand(this.baseExpression1.assign(var, expression), this.baseExpression2.assign(var, expression));
    }
}
