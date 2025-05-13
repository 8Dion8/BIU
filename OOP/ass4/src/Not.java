import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Not implements Expression {

    public Expression baseExpression;

    public Not(Expression expression) {
        this.baseExpression = expression;
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return !this.baseExpression.evaluate(assignment);
    }

    public Boolean evaluate() throws Exception {
        return this.evaluate(new HashMap<String, Boolean>());
    }

    public List<String> getVariables() {
        return this.baseExpression.getVariables();
    }

    public String toString() {
        return "~(" + this.baseExpression.toString() + ")";
    }

    public Expression assign(String var, Expression expression) {
        return new Not(this.baseExpression.assign(var, expression));
    }
}
