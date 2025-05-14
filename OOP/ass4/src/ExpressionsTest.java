/**
 * Tests the functionality of the Expression class and its various methods.
 * This includes creating an expression, assigning values to variables,
 * evaluating the expression, and testing its nandify, norify, and simplify methods.
 *
 * @author Gleb Shvartser 346832892
 */
public class ExpressionsTest {

    /**
    * The test itself.
    *
    * @throws Exception if an error occurs during evaluation or other operations.
    */
    public static void test() throws Exception {
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

    /**
     * The main method that serves as the entry point for the program.
     * It calls the Test method and handles any exceptions that may occur.
     *
     * @param args command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        try {
            ExpressionsTest.test();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
