abstract public class Test3 {
    public static void Test() throws Exception {
        Expression e1 = new Var("x");
        Expression e2 = new Var("y");
        Expression e1n = new Xor(e1, e2);
        Expression e2n = new Xor(e2, e1);

        System.out.println(e1.equals(e2));
        System.out.println(e1.equals(e1));
        System.out.println(e1.equals(e1n));
        System.out.println(e2.equals(e2n));
        System.out.println(e1n.equals(e2n));
        System.out.println(e2n.equals(e1n));
    }

    public static void main(String[] args) {
        try {
            Test3.Test();
        } catch(Exception e) {
            System.out.println(e);
        } finally {}
    }
}
