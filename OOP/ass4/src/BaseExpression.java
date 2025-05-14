import java.util.TreeMap;
import java.util.List;
import java.util.HashMap;

abstract class BaseExpression implements Expression {
    public Boolean evaluate() throws Exception {
        return this.evaluate(new TreeMap<String, Boolean>());
    }

    public Boolean equals(Expression expr) {
        List<String> variables = Common.merge(this.getVariables(), expr.getVariables());
        TruthTableTestIterator testIter = new TruthTableTestIterator(variables);
        Boolean resLeft = false;
        Boolean resRight = false;
        while (testIter.hasNext()) {
            HashMap<String, Boolean> testMap = testIter.next();
            try {
                resLeft = this.evaluate(testMap);
                resRight = expr.evaluate(testMap);
            } catch (Exception e) {
                System.err.println("Something has gone VERY wrong, check the iterator lol");
                System.out.println(e);
            }
            if (resLeft != resRight) {
                return false;
            }
        }
        return true;
    }
}
