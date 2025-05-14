import java.util.Map;
import java.util.List;

/**
 * This interface represents a logical expression that can be evaluated,
 * manipulated, and transformed. It provides methods for evaluating the
 * expression with variable assignments, retrieving variables, and converting
 * the expression into different logical forms.
 *
 * @author Gleb Shvartser 346832892
 */
public interface Expression {
    /**
    * Evaluates the expression using the variable values provided
    * in the assignment and returns the result.
    *
    * @param assignment a map containing variable names as keys and their
    *                   corresponding boolean values as values.
    * @return the result of evaluating the expression.
    * @throws Exception if the expression contains a variable that is not
    *                   present in the assignment.
    */
   Boolean evaluate(Map<String, Boolean> assignment) throws Exception;

   /**
    * Evaluates the expression using an empty assignment.
    *
    * @return the result of evaluating the expression.
    * @throws Exception if the expression contains a variable that cannot
    *                   be resolved in the empty assignment.
    */
   Boolean evaluate() throws Exception;

   /**
    * Retrieves a list of all variable names present in the expression.
    *
    * @return a list of variable names as strings.
    */
   List<String> getVariables();

   /**
    * Provides a string representation of the expression.
    *
    * @return a string that represents the expression.
    */
   String toString();

   /**
    * Creates a new expression in which all occurrences of the specified
    * variable are replaced with the provided expression. This does not
    * modify the current expression.
    *
    * @param var the variable to replace.
    * @param expression the expression to replace the variable with.
    * @return a new expression with the variable replaced.
    */
   Expression assign(String var, Expression expression);

   /**
    * Compares the current expression with another expression for equality.
    *
    * @param expr the expression to compare with.
    * @return true if the expressions are equivalent, false otherwise.
    */
   Boolean equals(Expression expr);

   /**
    * Converts the current expression into an equivalent expression
    * using only the NAND logical operator.
    *
    * @return a new expression that uses only NAND operations.
    */
   Expression nandify();

   /**
    * Converts the current expression into an equivalent expression
    * using only the NOR logical operator.
    *
    * @return a new expression that uses only NOR operations.
    */
   Expression norify();

   /**
    * Simplifies the current expression by applying logical simplifications
    * where possible.
    *
    * @return a simplified version of the expression.
    */
   Expression simplify();
}
