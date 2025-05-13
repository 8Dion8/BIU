import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Val implements Expression {

    private Boolean value;

    public Val(Boolean value) {
        this.value = value;
    }

    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return this.value;
    }

    public Boolean evaluate() throws Exception {
        return this.value;
    }

    public List<String> getVariables() {
        return new ArrayList<String>();
    }

    public String toString() {
        if (this.value) {
            return "T";
        } else {
            return "F";
        }
    }

    public Expression assign(String var, Expression expression) {
        return this;
    }
}
