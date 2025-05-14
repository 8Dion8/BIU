import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
* The Val class represents a boolean value in an expression.
* @author Gleb Shvartser 346832892
*/
public class Val implements Expression {

    private Boolean value;

    /**
    * Constructs a Val instance with the specified boolean value.
    *
    * @param value the boolean value to be assigned to this instance
    */
    public Val(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean equals(Expression expr) {
        return this.toString() == expr.toString();
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return this.value;
    }

    @Override
    public Boolean evaluate() throws Exception {
        return this.value;
    }

    @Override
    public List<String> getVariables() {
        return new ArrayList<String>();
    }

    @Override
    public String toString() {
        if (this.value) {
            return "T";
        } else {
            return "F";
        }
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return this;
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
