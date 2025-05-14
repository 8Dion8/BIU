import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Var implements Expression {

    private String variable;

    public Var(String variable) {
        this.variable = variable;
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        if (assignment.keySet().contains(this.variable)) {
            return assignment.get(this.variable);
        } else {
            throw new Exception("Variable not found in assignment.");
        }
    }

    public Boolean evaluate() throws Exception {
        return this.evaluate(new HashMap<String, Boolean>());
    }

    public List<String> getVariables() {
        List<String> list = new ArrayList<String>();
        list.add(this.variable);
        return list;
    }

    public String toString() {
        return this.variable;
    }

    public Expression assign(String var, Expression expression) {
        if (this.variable.equals(var)) {
            return expression;
        } else {
            return this;
        }
    }

        public Expression nandify() {
            return this;
        }

        public Expression norify() {
            return this;
        }
}
