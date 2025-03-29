import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class DataPoint {
  Set<String> tags;
  int timestamp;
  int value;
  public DataPoint(Set<String> tags, int timestamp, int value) {
    this.tags = tags;
    this.timestamp = timestamp;
    this.value = value;
  }
}
class Solution {
  List<Integer> calc(List<DataPoint> list, int k, String tag) {
    List<DataPoint> filteredList = new ArrayList<>();
    for (DataPoint dp : list) {
      if (dp.tags.contains(tag)) {
        filteredList.add(dp);
      }
    }

    filteredList.sort((d1, d2) -> d1.timestamp - d2.timestamp);
    List<Integer> result = new ArrayList<>();
    if (k > filteredList.size()) {
      throw new IllegalArgumentException("out of range");
    }

    int windowSum = 0;
    Queue<Integer> queue = new LinkedList<>();
    for (DataPoint dp : filteredList) {
      queue.offer(dp.value);
      windowSum += dp.value;
      if (queue.size() > k) {
        windowSum -= queue.poll();
      }
      if (queue.size() == k) {
        result.add(windowSum);
      }
    }
    return result;
  }
  
  public static void main(String[] args) {
    Solution solution = new Solution();
     List<DataPoint> input = Arrays.asList(
            new DataPoint(Set.of("env:prod", "prod1"), 1, -1),
            new DataPoint(Set.of("env:prod"), 10, 10),
            new DataPoint(Set.of("prod1"), 3, -10),
            new DataPoint(Set.of("prod1"), 100, 100),
            new DataPoint(Set.of("env:prod", "prod1"), 2, 2));
    List<Integer> output = solution.calc(input, 2, "prod1");
        System.out.println(output);            
  }
}
