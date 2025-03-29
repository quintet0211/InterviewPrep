import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Problem 2
class Solution {
  Map<String, Integer> countRepetition(String s) {
    Map<String, Integer> map = new HashMap<>();
    if (s == null || s.isEmpty())  {
      return map;
    }
    
    s = s.trim().toLowerCase().replace(",", " ").replace(".", " ");
    String[] words = s.split(" ");
    for (String word : words) {
      if (!word.trim().isBlank()) {
        map.put(word, map.getOrDefault(word, 0) + 1);
      }
    }
    return map;
  }

  public static void main(String[] args) {
    Solution solution = new Solution();
    String paragraph = "Hello, world. Hello 123, world 456.";
    Map<String, Integer> freq = solution.countRepetition(paragraph);
    System.out.println(freq);
  }
}
