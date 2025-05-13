import java.util.List;
import java.util.TreeMap;
import java.util.Map;

abstract public class Test1 {
    public static void Test() throws Exception {
        Expression e2 = new Xor(new And(new Var("x"), new Var("y")), new Val(true));
        String s = e2.toString();
        System.out.println(s);
        List<String> vars = e2.getVariables();
        for (String v : vars) {
           System.out.println(v);
        }
        Expression e3 = e2.assign("y", e2);
        System.out.println(e3);
        // ((x & ((x & y) ^ T)) ^ T)
        e3 = e3.assign("x", new Val(false));
        System.out.println(e3);
        // ((F & ((F & y) ^ T)) ^ T)
        Map<String, Boolean> assignment = new TreeMap<>();
        assignment.put("x", true);
        assignment.put("y", false);
        Boolean value = e2.evaluate(assignment);
        System.out.println("The result is: " + value);
    }

    public static void main(String[] args) {
        try {
            Test1.Test();
        } catch(Exception e) {
            System.out.println(e);
        } finally {}
    }
}
