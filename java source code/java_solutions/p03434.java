import java.util.*;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    ArrayList<Integer> ar = new ArrayList<Integer>();
    int N = sc.nextInt();
    for(int i=0;i<N;i++){
      int a = sc.nextInt();
      ar.add(a);
    }
    Collections.sort(ar,Comparator.reverseOrder());

    int Alice = 0;
    for(int i=0;i<N;i+=2){
      Alice += ar.get(i);
    }

    int Bob = 0;
    for(int i=1;i<N;i+=2){
      Bob += ar.get(i);
    }
    System.out.println(Alice - Bob);
  }
}
