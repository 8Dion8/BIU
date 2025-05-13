import java.util.TreeMap;
import java.util.List;

abstract public class BinaryExpression implements Expression {
    public Expression baseExpression1;
    public Expression baseExpression2;

    public Boolean evaluate() throws Exception {
        return this.evaluate(new TreeMap<String, Boolean>());
    }

    public List<String> getVariables() {
        List<String> variables1 = this.baseExpression1.getVariables();
        List<String> variables2 = this.baseExpression2.getVariables();
        for (String var: variables2) {
            if (!variables1.contains(var)) {
                variables1.add(var);
            }
        }
        return variables1;
    }

    public String toString(String representation) {
        return "(" + this.baseExpression1.toString() + representation + this.baseExpression2.toString() + ")";
    }
}
