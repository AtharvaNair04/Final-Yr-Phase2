import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    String[] str_array = new String[n];
    long[] cost_array = new long[n];
    PriorityQueue<Pair> queue = new PriorityQueue<>();
    for(int i = 0; i < n; i++){
      str_array[i] = sc.next();
      cost_array[i] = sc.nextLong();
      queue.add(new Pair(cost_array[i], str_array[i]));
    }
    sc.close();
    long ans = Long.MAX_VALUE;
    //無限ループ対応
    HashMap<String, Long> memo = new HashMap<>();

    while (!queue.isEmpty()){
      Pair p = queue.poll();
      if(p.cost > ans) {
        continue;
      }
      if(memo.containsKey(p.str)){
        if(memo.get(p.str) < p.cost){
          continue;
        }
      } else {
        memo.put(p.str, p.cost);
      }
      if(isPalindrome(p.str)){
        ans = Math.min(ans, p.cost);
        continue;
      }
      for(int i = 0; i < n; i++){
        String next = nextStatus(p.str, str_array[i]);
        if(next != null){
          queue.add(new Pair(p.cost + cost_array[i], next));
        }
      }
    }

    System.out.println(ans != Long.MAX_VALUE ? ans : -1);
  }

  static String nextStatus(String cur, String next){
    String left_str, right_str;
    if(cur.endsWith("_")){
      left_str = next;
      right_str = cur.substring(0, cur.length() - 1);
    } else if(cur.startsWith("_")){
      left_str = cur.substring(1);
      right_str = next;
    } else {
      left_str = cur;
      right_str = next;
    }
    int len = Math.min(left_str.length(), right_str.length());
    int idx = 0;
    while(idx < len){
      if(left_str.charAt(idx) != right_str.charAt(right_str.length() - 1 - idx)){
        return null;
      }
      idx++;
    }
    if(left_str.length() > right_str.length()){
      return "_" + left_str.substring(len);
    } else if(left_str.length() < right_str.length()){
      return right_str.substring(0, right_str.length() - len) + "_";
    } else {
      return "";
    }
  }

  static boolean isPalindrome(String str){
    str = str.replaceAll("_", "");
    if("".equals(str) || str.length() == 1){
      return true;
    }
    int left = 0;
    int right = str.length() - 1;
    while(left < right){
      if(str.charAt(left) != str.charAt(right)){
        return false;
      }
      left++;
      right--;
    }
    return true;
  }
}
class Pair implements Comparable<Pair> {
  long cost;
  String str;
  Pair(long cost, String str){
    this.cost = cost;
    this.str = str;
  }
  @Override
  public int compareTo(Pair o) {
    return this.cost != o.cost ? Long.compare(this.cost, o.cost) : this.str.compareTo(str);
  }
}
