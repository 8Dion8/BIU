abstract public class Test2 {
    public static void Test() throws Exception {
        Expression e = new Xor(new Xnor(new Var("x"), new Val(true)), new Nand(new Var("y"), new Val(false)));
        System.out.println(e.nandify());
        System.out.println(e.norify());
    }

    public static void main(String[] args) {
        try {
            Test2.Test();
        } catch(Exception e) {
            System.out.println(e);
        } finally {}
    }
}
