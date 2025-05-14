abstract public class Test4 {
    public static void Test() throws Exception {
        Expression x = new Var("x");
        Expression falseV = new Val(false);
        Expression trueV = new Val(true);

        System.out.println(new And(x, trueV).simplify());
        System.out.println(new And(x, falseV).simplify());
        System.out.println(new And(x, x).simplify());
        System.out.println(new Or(x, trueV).simplify());
        System.out.println(new Or(x, falseV).simplify());
        System.out.println(new Or(x, x).simplify());
        System.out.println(new Xor(x, trueV).simplify());
        System.out.println(new Xor(x, falseV).simplify());
        System.out.println(new Xor(x, x).simplify());
        System.out.println(new Nand(x, trueV).simplify());
        System.out.println(new Nand(x, falseV).simplify());
        System.out.println(new Nand(x, x).simplify());
        System.out.println(new Nor(x, trueV).simplify());
        System.out.println(new Nor(x, falseV).simplify());
        System.out.println(new Nor(x, x).simplify());
        System.out.println(new Xnor(x, x).simplify());

        Expression e = new Xor(new And(new Var("x"), new Val(false)), new Or(new Var("y"), new Val(false)));
        System.out.println(e);
        // the result is:
        // ((x & F) ^ (y | F))
        System.out.println(e.simplify());
        // the result is:
        // y
    }

    public static void main(String[] args) {
        try {
            Test4.Test();
        } catch(Exception e) {
            System.out.println(e);
        } finally {}
    }
}
