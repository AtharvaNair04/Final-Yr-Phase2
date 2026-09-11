import java.util.*;
public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int k = sc.nextInt();
    int[] b = new int[n];
    for(int i=0;i<k;i++) {
      int d = sc.nextInt();
      int[] a = new int[d];
      for(int j=0;j<d;j++){
        a[j]  = sc.nextInt();
        int c = a[j];
		b[c-1] = 1;
      }      
    }
    int count = 0;
    for(int x=0;x<b.length;x++) {
      if(b[x]==0) {
      count = count + 1;
      }
    }
    System.out.println(count);
  }
}
