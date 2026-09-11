import java.util.Scanner;

public class Main{
  public static void main(String[] args){
    Scanner in=new Scanner(System.in);
    int n=in.nextInt();
    int[] a=new int[n];
    int[] b=new int[n];
    int[] c=new int[n-1];
    for(int i=0;i<n;i++){
      a[i]=in.nextInt()-1;
    }
    for(int i=0;i<n;i++){
      b[i]=in.nextInt();
    }
    for(int i=0;i<n-1;i++){
      c[i]=in.nextInt();
    }
    int tmp=a[0];
    int res=b[tmp];
    
    for(int i=1;i<n;i++){
      res+=b[a[i]];
      res+=(tmp+1==a[i])?c[tmp]:0;
      tmp=a[i];
    }
    System.out.println(res);
  }
}
