import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Problem 1
// 给一个target sum，和[1,5,10,25]。问使用最少的数字个数来凑齐这个target sum。解答很简单：先用大数，再用小数，直到最后用1来凑齐。 
class Solution {
public int minCountGreedy(int target) {
  List<Integer> list = new ArrayList<>(){{
    add(1);
    add(5);
    add(10);
    add(25);
  }};
  list.sort((a, b) -> b - a);
  int count = 0;
  for (int num : list) {
    if (target == 0) {
      break;
    }
    count += target / num;
    target = target % num;
  }

  return count;
}

  public static void main(String[] args) {
    Solution solution = new Solution();
    System.out.println(solution.minCountGreedy(63)); // 输出 6
    System.out.println(solution.minCountGreedy(1));  // 输出 1
    System.out.println(solution.minCountGreedy(99)); // 输出 9 (25×3 + 10×2 + 1×4)
  }
}

