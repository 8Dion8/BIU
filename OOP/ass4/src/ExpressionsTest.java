public class ExpressionsTest {
    public static void Test() throws Exception {
        Expression e = new Xnor(
            new Var("x"),
            new Xor(
                new And(
                    new Var("y"),
                    new Var("z")
                ),
                new Val(true)
            )
        );

        System.out.println(e);

        Expression ea = e.assign("x", new Val(true));
        ea = ea.assign("y", new Val(false));
        ea = ea.assign("z", new Val(true));
        System.out.println(ea.evaluate());

        System.out.println(e.nandify());

        System.out.println(e.norify());

        System.out.println(e.simplify());
    }

    public static void main(String[] args) {
        try {
            ExpressionsTest.Test();
        } catch(Exception e) {
            System.out.println(e);
        } finally {}
    }
}
