import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TruthTableTestIterator implements Iterator<HashMap<String, Boolean>> {
    private ArrayList<Boolean> currentState;
    private ArrayList<String> variables;
    private int length;

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

    public void incrementState(int index) {
        if (currentState.get(index)) {
            currentState.set(index, false);
            if (index >= length-1) {
                return;
            }
            incrementState(index+1);
        } else {
            currentState.set(index, true);
        }
    }

    @Override
    public boolean hasNext() {
        for (Boolean c: currentState) {
            if (!c) {
                return true;
            }
        }
        return false;
    }

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
