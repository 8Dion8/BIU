import java.util.TreeMap;

abstract class BaseExpression implements Expression {
    public Boolean evaluate() throws Exception {
        return this.evaluate(new TreeMap<String, Boolean>());
    }
}
