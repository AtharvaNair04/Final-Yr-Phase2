import java.util.*;
import java.io.*;
class Main{
  static long count =0;
  static int n=0;
  public static void main(String args[])throws Exception{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    n = Integer.parseInt(br.readLine());
    String str [] = br.readLine().split("\\s+");
    int array [] = new int[n];
    for(int i = 0; i<n; i++){
      array[i] = Integer.parseInt(str[i]);
    }
    merge_sort(array,0,array.length);
    System.out.println(count);
  }
  //TLE
  // static void Inversions(int A[]){
  //   for(int i = 0; i<n-1; i++){
  //     for(int j = i+1;j<n;j++){
  //         if(A[i]>A[j])count++;
  //     }
  //   }
  // }

  //TLE
  // static void Inversions(int A[]){
  //   int max = 0;
  //   int max_n = 0;
  //   int prev = 99999;
  //   for(int j =0; j<A.length;j++){
  //     max = 0;
  //
  //     max_n = 0;
  //     for(int i = 0; i < A.length; i++){
  //       if(max<A[i]&&prev>A[i]){
  //         max = A[i];
  //         max_n=i;
  //       }
  //     }
  //     for(int i = max_n+1; i <A.length; i++){
  //       if(A[i]<max)count++;
  //     }
  //     prev = A[max_n];
  //   }
  // }

  static void merge(int A[], int left, int mid, int right){
    int n1 = mid - left;
    int n2 = right - mid;
    int L[] = new int[n1+1];
    int R[] = new int[n2+1];
    for(int p = left, k= 0; p < mid; k++, p++){
      L[k] = A[p];
    }
    for(int p =mid, k =0; p < right; k++, p++){
      R[k] = A[p];
    }
    L[n1] = (int)Double.POSITIVE_INFINITY;
    R[n2] = (int)Double.POSITIVE_INFINITY;
    int i = 0;
    int j = 0;
    for(int k = left; k<right; k++){
      if(L[i]>R[j]){
        A[k] = R[j++];
        count += (mid - left - i);
      }else{
        A[k] = L[i++];

      }
    }
  }

  static void merge_sort(int A[], int left, int right){
    if(right-left<2)return;
    int mid = (left+right)/2;
    merge_sort(A,left, mid);
    merge_sort(A,mid, right);
    merge(A, left, mid, right);
  }
}
