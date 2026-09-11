import java.util.Arrays;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int numberOfTree = sc.nextInt();
    int numberOfUse = sc.nextInt();
    int[] tree = new int[numberOfTree];
    for (int i = 0; i < tree.length; i++) {
      tree[i] = sc.nextInt();
    }
    Arrays.sort(tree);
    int min = tree[tree.length - 1] - tree[0];
    
    for (int i = 0; i < tree.length - numberOfUse + 1; i++) {
      if (tree[i + numberOfUse - 1] - tree[i] < min) {
        min = tree[i + numberOfUse - 1] - tree[i];
      }
    }
    System.out.println(min);
  }

}
