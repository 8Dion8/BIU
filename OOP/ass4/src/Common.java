import java.util.ArrayList;
import java.util.List;

public class Common {
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
