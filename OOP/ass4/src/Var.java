import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * The Var class represents a variable in a logical expression.
 * @author Gleb Shvartser 346832892
 */
public class Var implements Expression {

    private String variable;

    /**
     * Constructs a Var object with the specified variable name.
     *
     * @param variable the name of the variable
     */
    public Var(String variable) {
        this.variable = variable;
    }

    @Override
    public Boolean equals(Expression expr) {
        return this.toString() == expr.toString();
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        if (assignment.keySet().contains(this.variable)) {
            return assignment.get(this.variable);
        } else {
            throw new Exception("Variable not found in assignment.");
        }
    }

    @Override
    public Boolean evaluate() throws Exception {
        return this.evaluate(new HashMap<String, Boolean>());
    }

    @Override
    public List<String> getVariables() {
        List<String> list = new ArrayList<String>();
        list.add(this.variable);
        return list;
    }

    @Override
    public String toString() {
        return this.variable;
    }

    @Override
    public Expression assign(String var, Expression expression) {
        if (this.variable.equals(var)) {
            return expression;
        } else {
            return this;
        }
    }

    @Override
    public Expression nandify() {
        return this;
    }

    @Override
    public Expression norify() {
        return this;
    }

    @Override
    public Expression simplify() {
        return this;
    }
}
