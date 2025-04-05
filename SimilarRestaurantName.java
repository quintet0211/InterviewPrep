/*
In DoorDash, the data importer occasionally makes typos, resulting in restaurant names that are very similar but not identical. Your task is to identify all restaurant names from a given list that are similar to a specified target name.
A restaurant name is considered similar if it can be transformed into the target name by swapping at most one pair of characters within the name.Example:
Input:
Target restaurant name: "burgerking"
List of restaurant names: ["burgreking", "burgerkin", "burgekirng", "kingburger"]
Output:
["burgreking", "burgekirng"]
(Both names can be corrected to "burgerking" by swapping a single pair of letters.)
Input:
A string representing the target restaurant name.
A list of strings representing restaurant names.
Output:
A list of restaurant names from the given list that are similar to the target name.

*/

import java.util.*;

public class RestaurantNameMatcher {

    public static List<String> findSimilarNames(String target, List<String> names) {
        List<String> result = new ArrayList<>();

        for (String name : names) {
            if (isSimilar(name, target)) {
                result.add(name);
            }
        }

        return result;
    }

    private static boolean isSimilar(String name, String target) {
        if (name.length() != target.length() || name.equals(target)) return false;

        List<Integer> diffs = new ArrayList<>();

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) != target.charAt(i)) {
                diffs.add(i);
            }
        }

        if (diffs.size() != 2) return false;

        int i = diffs.get(0);
        int j = diffs.get(1);

        // Swap name[i] and name[j] and check if it becomes target
        return name.charAt(i) == target.charAt(j) && name.charAt(j) == target.charAt(i);
    }

    public static void main(String[] args) {
        String target = "burgerking";
        List<String> names = Arrays.asList("burgreking", "burgerkin", "burgekirng", "kingburger");

        List<String> similarNames = findSimilarNames(target, names);
        System.out.println(similarNames); // Output: [burgreking, burgekirng]
    }
}
