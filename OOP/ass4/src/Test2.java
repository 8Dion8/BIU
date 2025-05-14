abstract public class Test2 {
    public static void Test() throws Exception {
        Expression e = new Xor(new Xnor(new Var("a"), new Var("b")), new Nand(new Var("c"), new Var("d")));
        System.out.println(e.nandify());
        System.out.println(e.norify());

        TruthTableTestIterator iter = new TruthTableTestIterator(e.getVariables());
        for (int i = 0; i < 10; i++) {
            System.out.println(iter.next());
        }
    }

    public static void main(String[] args) {
        try {
            Test2.Test();
        } catch(Exception e) {
            System.out.println(e);
        } finally {}
    }
}
