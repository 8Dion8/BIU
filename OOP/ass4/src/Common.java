import java.util.ArrayList;
import java.util.List;

/**
 * The Common class provides common utility methods.
 *
 * @author Gleb Shvartser 346832892
 */
public class Common {

    /**
     * Merges two lists of strings into a single list, ensuring no duplicates.
     *
     * @param arr1 the first list of strings
     * @param arr2 the second list of strings
     * @return a new list containing all unique elements from both input lists
     */
    public static List<String> merge(List<String> arr1, List<String> arr2) {
        List<String> newArr = new ArrayList<String>(arr1);
        for (String var: arr2) {
            if (!newArr.contains(var)) {
                newArr.add(var);
            }
        }
        return newArr;
    }
}
