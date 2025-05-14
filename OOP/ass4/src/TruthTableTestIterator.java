import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
* The TruthTableTestIterator class represents an iterator used to generate every possible combination
* of variable values. This is used for testing the equality of Expressions by comparing truth tables
* @author Gleb Shvartser 346832892
*/
public class TruthTableTestIterator implements Iterator<HashMap<String, Boolean>> {
    private ArrayList<Boolean> currentState;
    private ArrayList<String> variables;
    private int length;

    /**
    * Constructs a TruthTableTestIterator with the specified list of variables.
    * Each variable represents a boolean value in the truth table.
    *
    * @param variables A list of variable names for which the truth table combinations will be generated.
    */
    public TruthTableTestIterator(List<String> variables) {
        this.length = 0;
        this.variables = new ArrayList<String>();
        this.currentState = new ArrayList<Boolean>();
        for (String variable: variables) {
            this.variables.add(variable);
            this.currentState.add(false);
            this.length++;
        }
    }

    /**
    * Increments the current state of the truth table iterator at the specified index.
    * This method updates the boolean values to generate the next combination of truth table values.
    *
    * @param index The index of the current state to increment.
    */
    public void incrementState(int index) {
        if (currentState.get(index)) {
            currentState.set(index, false);
            if (index >= length - 1) {
                return;
            }
            incrementState(index + 1);
        } else {
            currentState.set(index, true);
        }
    }

    /**
    * Checks if there are more combinations of truth table values to iterate over.
    *
    * @return true if there are more combinations, false otherwise.
    */
    @Override
    public boolean hasNext() {
        for (Boolean c: currentState) {
            if (!c) {
                return true;
            }
        }
        return false;
    }

    /**
    * Generates the next combination of truth table values as a mapping of variable names to boolean values.
    *
    * @return A HashMap representing the next combination of variable values.
    */
    @Override
    public HashMap<String, Boolean> next() {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        for (int i = 0; i < this.length; i++) {
            map.put(variables.get(i), currentState.get(i));
        }

        this.incrementState(0);

        return map;
    }
}
