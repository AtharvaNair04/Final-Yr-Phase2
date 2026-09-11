import java.util.*;
class Main{
  static int count=0;
  public static void main(String args[]){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int [] Array = new int[n];
    for(int i = 0; i<n; i++){
      Array[i] = sc.nextInt();
    }

    System.arraycopy(Array,0,shell_sort(Array, n),0,n);
    for(int i =0; i<n; i++){
      System.out.println(Array[i]);
    }
  }

  static int [] insertion_sort(int [] A, int n, int g){
    int value;
    int j;
    for(int i =g; i<n;i++){
      value = A[i];
      j = i-g;
      while(j>=0 && A[j]>value){
        A[j+g] = A[j];
        j = j-g;
        count++;
      }
      A[j+g] = value;
    }
    return A;
  }

  static int [] shell_sort(int [] A, int n){
    //Gの逆順の配列
    int [] B = new int[n];
    int m = 0;
    for(int i = 1; i <= n; i = i * 3 + 1) {
    B[m] = i;
    m++;
  }
  //Gの配列にBを逆順で入れる
  int [] G = new int[m];
  int j = 0;
  for(int i = m-1; i>=0;i--){
    G[j] = B[i];
    j++;
  }
    System.out.println(m);
    for(int i =0; i<m; i++){
      System.arraycopy(A,0,insertion_sort(A, n, G[i]),0,n);
      if(i!=m-1)System.out.printf("%d ",G[i]);
    }
    System.out.println(G[m-1]);
    System.out.println(count);
    return A;
  }
}
