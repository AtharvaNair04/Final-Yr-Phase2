import java.util.*;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    ArrayList<Integer> ar = new ArrayList<Integer>();

    for(int i=0;i<3;i++){
      int A = sc.nextInt();
      ar.add(A);
    }
    Collections.sort(ar);
    int task1 = ar.get(1) - ar.get(0);
    int task2 = ar.get(2) - ar.get(1);
    System.out.println(task1 + task2);
  }
}
